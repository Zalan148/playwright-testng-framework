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
  let payload = JSON.stringify({ username: 'testuser', password: 'password123' });
  let res = http.post('https://your-api-endpoint.com/login', payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  check(res, { 'login success': (r) => r.status === 200 });
  sleep(1);
}
