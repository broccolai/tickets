import cors from 'cors';
import express from 'express';

import auth from './providers/auth';
import announce from './routes/announce';
import legacy from './routes/legacy';
import status from './routes/status';

const app = express();

app.use(express.json());
app.use(cors());

app.use('/', legacy);

const api = '/api/v1';

app.use(api + '/status', status);
app.use(api + '/announce', auth, announce);

export default app;
