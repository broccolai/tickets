import client from './lib/client';
import app from './lib/app';

require('dotenv').config();

client.login(process.env.DISCORD_ACCESS_TOKEN);

app.listen(process.env.PORT, () => console.log(`App listening!`));
