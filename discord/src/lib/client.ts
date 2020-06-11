import * as crypto from 'crypto';
import { Client, MessageEmbed } from 'discord.js';

import db from './database';
import { servers } from './storage';

const client = new Client();

client.on('message', async (message) => {
  if (message.author.bot) return;

  if (message.content.indexOf('!') !== 0) return;

  const args = message.content.slice('!'.length).trim().split(/ +/g);
  const command = args.shift().toLowerCase();

  if (command == 'setup') {
    if (!message.member.hasPermission('ADMINISTRATOR')) {
      message.reply('Only administrators can use this command');
      return;
    }

    let token: string;
    let output: string;

    if (servers.has(message.guild.id)) {
      const data = servers.get(message.guild.id);

      token = data['token'];
      output = data['output'];
    } else {
      token = crypto.randomBytes(30).toString('hex');
      output = message.channel.id;
      db.run('INSERT INTO server(guild, token, outputChannel) VALUES(?, ?, ?)', message.guild.id, token, output);
    }

    servers.set(message.guild.id, { token: token, output: output });

    const embed = new MessageEmbed()
      .setColor('#0099ff')
      .setTitle('Pure Tickets Integration Setup')
      .addField(':ticket:  Your Token', token)
      .addField(':house:  Guild Id', message.guild.id, true)
      .addField(':office:  Output Channel', output, true);

    message.member.send(embed);
    message.member.send(
      'Keep your guilds token secret. if you need to change the broadcast channel, re-run setup in the desired channel',
    );
  }
});

export default client;
