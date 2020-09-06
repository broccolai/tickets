import one from '@api/v1/router';
import two from '@api/v2/router';
// import web from '@api/web/router';
import cors from 'cors';
import express from 'express';

const app = express();

app.use(express.json());
app.use(cors());

app.use(one);
app.use(two);
// app.use(web);

export default app;
