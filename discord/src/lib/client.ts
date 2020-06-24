import { ActivityOptions, Client } from 'discord.js';

import PureGuild from '../constructs/pureGuild';
import db from './database';
import { servers } from './storage';

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

    const guild = new PureGuild(message.guild.id, token, output);

    servers.set(guild.id, guild);

    const embed = new MessageEmbed()
      .setColor('#0099ff')
      .setTitle('Pure Tickets Integration Setup')
      .addField(':ticket:  Your Token', guild.token)
      .addField(':house:  Guild Id', guild.id, true)
      .addField(':office:  Output Channel', guild.output, true);

    message.member.send(embed);
    message.member.send(
      'Keep your guilds token secret. if you need to change the broadcast channel, re-run setup in the desired channel',
    );
    message.channel.send(':envelope: You have been sent a private message with setup information');
  }
});

export default client;
