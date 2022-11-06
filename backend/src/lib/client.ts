import { Client, Events, IntentsBitField, REST, Routes } from 'discord.js';
import { ActivityType } from 'discord-api-types/v10';
import { create, invoke } from '@lib/commands/setup';

const client = new Client({
  intents: [IntentsBitField.Flags.Guilds],
});

client.on('ready', () => {
  const update = () => {
    client.user.setPresence({
      status: 'online',
      activities: [
        {
          name: 'Tickets',
          type: ActivityType.Watching,
        },
      ],
    });
  };

  update();
  setInterval(update, 300000);
});

client.on(Events.InteractionCreate, async (interaction) => {
  if (!interaction.isChatInputCommand()) {
    return;
  }

  if (interaction.commandName == 'setup') {
    await invoke(interaction);
  }
});

const rest = new REST({ version: '10' }).setToken(process.env.DISCORD_ACCESS_TOKEN);

rest.put(Routes.applicationCommands(process.env.DISCORD_CLIENT_ID), {
  body: [create()],
});

export default client;
