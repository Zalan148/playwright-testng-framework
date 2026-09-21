import fs from 'fs';
const summary = JSON.parse(fs.readFileSync('./results/summary.json'));
console.log(JSON.stringify(summary, null, 2));
