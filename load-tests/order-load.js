/*
  HybridOrderFlowTest
  --------------------
  This test uses mock frontend pages (served from /public via Node server on port 3001).

  Reason: SauceDemo requires login before accessing inventory/orders pages.
  To isolate and load-test flows (login, browse, order) without full login + checkout,
  we use mock pages instead.
*/

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 20,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<800'],
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
  let res = http.post('http://localhost:3001/orders.html', JSON.stringify({ item: 'Laptop' }), {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'status is 201': (r) => r.status === 201 });
  sleep(1);
}
