import * as crypto from 'crypto';

import PureGuild from '../constructs/PureGuild';
import db from '../providers/database';
import { servers } from '../providers/storage';
import { CommandInteraction, EmbedBuilder, SlashCommandBuilder } from 'discord.js';

const create = () => {
  const command = new SlashCommandBuilder()
    .setName('setup')
    .setDescription('setup pure tickets discord bot')
    .setDefaultMemberPermissions(0)
    .setDMPermission(false);

  return command.toJSON();
};

const invoke = async (interaction: CommandInteraction) => {
  let token: string;
  let output: string;

  if (servers.has(interaction.guild.id)) {
    const data = servers.get(interaction.guild.id);

    token = data['token'];
    output = interaction.channel.id;

    db.run('UPDATE server SET outputChannel = ? WHERE guild = ?', output, interaction.guild.id);
  } else {
    token = crypto.randomBytes(30).toString('hex');
    output = interaction.channel.id;
    db.run('INSERT INTO server(guild, token, outputChannel) VALUES(?, ?, ?)', interaction.guild.id, token, output);
  }

  const guild = new PureGuild(interaction.guild.id, token, output);

  servers.set(guild.id, guild);

  const embed = new EmbedBuilder()
    .setColor(0x0099ff)
    .setTitle('Pure Tickets Integration Setup')
    .addFields(
      { name: ':ticket:  Your Token', value: guild.token },
      { name: ':house:  Guild Id', value: guild.id, inline: true },
      { name: ':office:  Output Channel', value: guild.output, inline: true },
    );

  await interaction.reply({
    content: 'Keep your guilds token secret. if you need to change the broadcast channel, re-run setup in the desired channel',
    embeds: [embed],
    ephemeral: true,
  });
};

export { create, invoke };
