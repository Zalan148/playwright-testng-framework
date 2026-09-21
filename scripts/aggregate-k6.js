import fs from 'fs';
import path from 'path';

const resultsDir = './results';
const files = fs.readdirSync(resultsDir).filter(f => f.endsWith('.json'));

let summary = {
  scenarios: [],
  totals: { requests: 0, errors: 0, avgLatency: 0 }
};

let thresholdFailed = false;

files.forEach(file => {
  const data = JSON.parse(fs.readFileSync(path.join(resultsDir, file)));
  const metrics = data.metrics;

  summary.scenarios.push({
    name: file,
    requests: metrics.http_reqs.count,
    errors: metrics.http_req_failed.count,
    p95Latency: metrics.http_req_duration.values['p(95)']
  });

  summary.totals.requests += metrics.http_reqs.count;
  summary.totals.errors += metrics.http_req_failed.count;
  summary.totals.avgLatency += metrics.http_req_duration.values['avg'];

  // Threshold highlighting
    console.log(`Scenario: ${file}`);
    console.log(`Requests: ${metrics.http_reqs.count}`);
    console.log(`Errors: ${metrics.http_req_failed.count}`);
    console.log(`p95 Latency: ${metrics.http_req_duration.values['p(95)']}ms`);

    if (metrics.http_req_duration.values['p(95)'] > 1000) {
      console.log("⚠️ Latency threshold exceeded (p95 > 1000ms)");
      thresholdFailed = true;
    }
    if ((metrics.http_req_failed.count / metrics.http_reqs.count) > 0.02) {
      console.log("❌ Error rate threshold exceeded (>2%)");
      thresholdFailed = true;
    }
    console.log("----");

});

// Average latency across scenarios
summary.totals.avgLatency = summary.totals.avgLatency / files.length;
fs.writeFileSync('./results/summary.json', JSON.stringify(summary, null, 2));


// Exit with failure if thresholds breached
if (thresholdFailed) {
  console.error("❌ One or more thresholds exceeded. Failing job.");
  process.exit(1);
}
