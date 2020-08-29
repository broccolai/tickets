import express from 'express';

import client from '@lib/client';

const router = express.Router();

router.get('/count', async (_req, res) => {
  res.send(client.guilds.cache.size.toString());
});

export default router;
