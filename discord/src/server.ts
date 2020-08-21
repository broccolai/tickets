import 'dotenv/config.js';
import app from './lib/app';
import client from './lib/client';

client.token = process.env.DISCORD_ACCESS_TOKEN;
client.connect();

app.listen(process.env.PORT, () => console.log(`App listening!`));
