import express from 'express';

import client from '../client';

const router = express.Router();

router.get('/count', async (_req, res) => {
  res.send(client.guilds.size.toString());
});

export default router;
