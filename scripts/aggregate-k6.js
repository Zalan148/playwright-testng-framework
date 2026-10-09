
import fs from 'fs';
import path from 'path';

const resultsDir = './results';
const files = [];

function findJsonFiles(dir) {
  if (!fs.existsSync(dir)) {
    throw new Error(`Results directory not found: ${dir}`);
  }

  for (const item of fs.readdirSync(dir)) {
    const fullPath = path.join(dir, item);
    const stat = fs.statSync(fullPath);

    if (stat.isDirectory()) {
      findJsonFiles(fullPath);
    } else if (
      item.endsWith('.json') &&
      item !== 'summary.json'
    ) {
      files.push(fullPath);
    }
  }
}

findJsonFiles(resultsDir);

if (files.length === 0) {
  throw new Error(`No k6 JSON result files found in ${resultsDir}`);
}

const summary = {
  scenarios: [],
  totals: {
    requests: 0,
    errors: 0,
    avgLatency: 0
  }
};

let totalDuration = 0;
let totalDurationCount = 0;
let thresholdFailed = false;

for (const file of files) {
  const lines = fs.readFileSync(file, 'utf8')
    .split(/\r?\n/)
    .filter(line => line.trim() !== '');

  let requests = 0;
  let errors = 0;
  const durations = [];

  for (const line of lines) {
    let event;

    try {
      event = JSON.parse(line);
    } catch (error) {
      throw new Error(
        `Invalid JSON in ${file}: ${error.message}`
      );
    }

    if (event.type !== 'Point' || !event.metric || !event.data) {
      continue;
    }

    const value = event.data.value;

    if (event.metric === 'http_reqs') {
      requests += value;
    }

    if (
      event.metric === 'http_req_failed' &&
      value === 1
    ) {
      errors += 1;
    }

    if (
      event.metric === 'http_req_duration' &&
      Number.isFinite(value)
    ) {
      durations.push(value);
    }
  }

  durations.sort((a, b) => a - b);

  const avgLatency = durations.length
    ? durations.reduce((sum, value) => sum + value, 0) /
      durations.length
    : 0;

  const p95Latency = durations.length
    ? durations[Math.ceil(durations.length * 0.95) - 1]
    : 0;

  const errorRate = requests > 0 ? errors / requests : 0;

  summary.scenarios.push({
    name: file,
    requests,
    errors,
    errorRate: Number((errorRate * 100).toFixed(2)),
    avgLatency: Number(avgLatency.toFixed(2)),
    p95Latency: Number(p95Latency.toFixed(2))
  });

  summary.totals.requests += requests;
  summary.totals.errors += errors;
  totalDuration += durations.reduce(
    (sum, value) => sum + value, 0
  );
  totalDurationCount += durations.length;

  console.log(`Scenario: ${file}`);
  console.log(`Requests: ${requests}`);
  console.log(`Errors: ${errors}`);
  console.log(`Error rate: ${(errorRate * 100).toFixed(2)}%`);
  console.log(`Average latency: ${avgLatency.toFixed(2)}ms`);
  console.log(`p95 latency: ${p95Latency.toFixed(2)}ms`);

  if (p95Latency > 1000) {
    console.log('⚠️ Latency threshold exceeded (p95 > 1000ms)');
    thresholdFailed = true;
  }

  if (errorRate > 0.02) {
    console.log('❌ Error rate threshold exceeded (>2%)');
    thresholdFailed = true;
  }

  console.log('----');
}

summary.totals.avgLatency = totalDurationCount
  ? Number((totalDuration / totalDurationCount).toFixed(2))
  : 0;

fs.writeFileSync(
  path.join(resultsDir, 'summary.json'),
  JSON.stringify(summary, null, 2)
);

console.log(`Summary written to ${path.join(resultsDir, 'summary.json')}`);

if (thresholdFailed) {
  console.error('❌ One or more thresholds exceeded. Failing job.');
  process.exit(1);
}
