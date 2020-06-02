import client from './lib/client';
import app from './lib/app';

require('dotenv').config();

client.login(process.env.DISCORD_ACCESS_TOKEN);

app.listen(80, () => console.log(`App listening on port $80!`));
