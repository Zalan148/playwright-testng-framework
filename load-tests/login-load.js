/*
  HybridLoginFlowTest
  --------------------
  This test uses mock frontend pages (served from /public via Node server on port 3001).

  Reason: SauceDemo requires login before accessing inventory/orders pages.
  To isolate and load-test flows (login, browse, order) without full login + checkout,
  we use mock pages instead.
*/

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 30,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    http_req_failed: ['rate<0.02'],
  },
};

export default function () {
  let payload = JSON.stringify({ username: 'admin_user', password: 'admin_password123' });
  let res = http.post('http://localhost:3001/auth/login', payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'login success': (r) => r.status === 200 });
  sleep(1);
}
