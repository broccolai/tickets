import { Client, ClientEvents, Message } from '@klasa/core';
import setup from './commands/setup';

const client = new Client();
const PREFIX = process.env.DISCORD_PREFIX || '!'

client.on(ClientEvents.Ready, async () => {
  setInterval(async () => {
    const name = client.guilds.size + ' servers';

    client.user.presence.modify((builder) => builder.setStatus('online').setGame((game) => game.setName(name)));
  }, 300000);
});

client.on(ClientEvents.MessageCreate, async (message: Message) => {
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
