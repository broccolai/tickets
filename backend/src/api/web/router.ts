import express from 'express';

import tickets from './routes/tickets';

const router = express.Router();

const path = '/api/web';

router.use(path + '/tickets', tickets);

export default router;
