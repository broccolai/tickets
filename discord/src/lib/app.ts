import cors from 'cors';
import express from 'express';

import announce from '../apis/v1/routes/announce';
import status from '../apis/v1/routes/status';
import auth from './providers/auth';

const app = express();

app.use(express.json());
app.use(cors());

const api = '/api/v1';

app.use(api + '/status', status);
app.use(api + '/announce', auth, announce);

export default app;
