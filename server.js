const jsonServer = require('json-server');
const path = require('path');

const server = jsonServer.create();
const router = jsonServer.router('db.json');
const middlewares = jsonServer.defaults({ static: path.join(__dirname, 'public') });

server.use(middlewares);
server.use(jsonServer.bodyParser);

server.post('/auth/login', (req, res) => {
  const { username, password } = req.body;
  const user = router.db.get('auth').find({ username, password }).value();
  if (!user) return res.status(401).json({ message: 'Invalid username or password' });
  return res.status(200).json({ token: user.token });
});

server.post('/orders', (req, res) => {
  console.log('Order received:', req.body);
  res.status(201).json({ message: 'Order created successfully' });
});

server.get('/orders/:id', (req, res) => {
  res.status(200).json({
    id: req.params.id,
    item: 'Laptop'
  });
});

server.use(router);

server.listen(3001, () => {
  console.log('Mock API + Frontend running on http://localhost:3001');
});

