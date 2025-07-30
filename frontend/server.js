import { createServer } from 'https';
import { parse } from 'url';
import { readFileSync } from 'fs';
import next from 'next';

const dev = process.env.NODE_ENV !== 'production';
const app = next({ dev });
const handle = app.getRequestHandler();

const httpsOptions = {
  key: readFileSync('./localhost+2-key.pem'),
  cert: readFileSync('./localhost+2.pem'),
};

app.prepare().then(() => {
  createServer(httpsOptions, async (req, res) => {
    const parsedUrl = parse(req.url, true);
    await handle(req, res, parsedUrl);
  }).listen(3000, () => {
    console.log('🚀 Ready on https://localhost:3000');
  });
});
