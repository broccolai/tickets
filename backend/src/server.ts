import 'dotenv/config.js';
import 'module-alias/register';
import app from '@lib/app';
import client from '@lib/client';

client
  .login(process.env.DISCORD_ACCESS_TOKEN)
  .then(() => console.info('PureTickets connected to Discord with token'))
  .catch(() => console.error('PureTickets backend could not connect to Discord, check your token'));

const port = process.env.PORT || 10400;
app.listen(port, () => console.info(`PureTickets backend running on port ${port}`));
