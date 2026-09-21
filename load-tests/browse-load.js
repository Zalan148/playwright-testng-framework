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
    'https://your-ui-endpoint.com/home',
    'https://your-ui-endpoint.com/products',
    'https://your-ui-endpoint.com/cart',
  ];

  for (let page of pages) {
    let res = http.get(page);
    check(res, { [`${page} status`]: (r) => r.status === 200 });
    sleep(1);
  }
}
