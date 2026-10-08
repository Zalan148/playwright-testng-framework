/*
  HybridLoadFlowTest
  --------------------
  This test uses mock frontend pages (served from /public via Node server on port 3001).

  Reason: SauceDemo requires login before accessing inventory/orders pages.
  To isolate and load-test flows (login, browse, order) without full login + checkout,
  we use mock pages instead.
*/

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 20,          // 20 virtual users
  duration: '20s',  // run for 20 seconds
  thresholds: {
    http_req_duration: ['p(95)<800'], // 95% of requests < 800ms
    http_req_failed: ['rate<0.01'],   // <1% errors
  },
};

export default function () {
  let res = http.get('http://localhost:3001/index.html');
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}
