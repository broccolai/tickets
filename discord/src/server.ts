import 'dotenv/config.js';

import app from './lib/app';
import client from './lib/client';

client.login(process.env.DISCORD_ACCESS_TOKEN);

app.listen(process.env.PORT, () => console.log(`App listening!`));
