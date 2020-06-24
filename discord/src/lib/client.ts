import { ActivityOptions, Client } from 'discord.js';

import setup from './commands/setup';

const client = new Client();

client.once('ready', () => {
  const options: ActivityOptions = {};

  options.name = client.guilds.cache.size.toString() + ' servers';
  options.type = 'WATCHING';

  client.user.setActivity(options);
});

client.on('message', async (message) => {
  if (message.author.bot) return;

  if (message.content.indexOf('!') !== 0) return;

  const args = message.content.slice('!'.length).trim().split(/ +/g);
  const command = args.shift().toLowerCase();

  switch (command) {
    case 'setup':
      setup(message);
      break;

    default:
      break;
  }
});

export default client;
