import cors from 'cors';
import express from 'express';

import auth from './providers/auth';
import announce from './routes/announce';
import status from './routes/status';

const app = express();

app.use(express.json());
app.use(cors());

const api = '/api/v1';

app.use(api + '/status', status);
app.use(api + '/announce', auth, announce);

export default app;
