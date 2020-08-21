import cors from 'cors';
import express from 'express';

import one from '../apis/v1/one';
import two from '../apis/v2/two';

const app = express();

app.use(express.json());
app.use(cors());

app.use(one);
app.use(two);

export default app;
