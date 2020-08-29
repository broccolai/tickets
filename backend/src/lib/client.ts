import { Client, Message } from 'discord.js';
import setup from './commands/setup';

const client = new Client();
const PREFIX = process.env.DISCORD_PREFIX || '!';

client.on('ready', () => {
  const update = () => {
    const name = client.guilds.cache.size + ' servers';

    client.user.setStatus('online');
    client.user.setActivity(name);
  };

  update();
  setInterval(update, 300000);
});

client.on('message', async (message: Message) => {
  if (message.author.bot || !message.content.startsWith(PREFIX)) {
    return;
  }

  const cmd = message.content.slice(1).trim().split(' ').shift().toLowerCase();

  switch (cmd) {
    case 'setup':
      setup(message);
      break;

    default:
      break;
  }
});

export default client;
