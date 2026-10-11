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

const orderData =
  JSON.parse(open('./load-tests/order-data.json'));


export let options = {
  vus: 20,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<800'],
    http_req_failed: ['rate<0.01'],
  },
};


const orderId = orderData.orderId;

export default function () {

  let res = http.get(
    `http://localhost:3001/orders/${orderId}`
  );

  check(res, {
    'status is 200': (r) => {
      if (r.status !== 200) {
        console.log(`Unexpected status: ${r.status}`);
        console.log(`Response body: ${r.body}`);
      }
      return r.status === 200;
    }
  });

  sleep(1);
}