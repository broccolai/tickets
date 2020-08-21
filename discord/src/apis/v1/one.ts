import express from 'express';

import auth from '../../lib/providers/auth';
import announce from './routes/announce';
import status from './routes/status';

const router = express.Router();

const path = '/api/v1';

router.use(path + '/status', status);
router.use(path + '/announce', auth, announce);

export default router;
