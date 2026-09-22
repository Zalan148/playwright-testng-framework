/*
  HybridBrowseFlowTest
  --------------------
  This test uses mock frontend pages (products.html, cart.html)
  served by our Node server on port 3001.

  Reason: SauceDemo requires login before accessing inventory/orders.
  To isolate and load-test browse/order flows without full login + checkout,
  we use mock pages instead.
*/

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 40,
  duration: '45s',
  thresholds: {
    http_req_duration: ['p(95)<1200'],
    http_req_failed: ['rate<0.03'],
  },
};

export default function () {
  let pages = [
    'http://localhost:3001/index.html',
    'http://localhost:3001/products.html',
    'http://localhost:3001/cart.html',
  ];

  for (let page of pages) {
    let res = http.get(page);
    check(res, { [`${page} status`]: (r) => r.status === 200 });
    sleep(1);
  }
}
