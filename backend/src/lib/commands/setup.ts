import * as crypto from 'crypto';

import { Embed, Message } from '@klasa/core';

import PureGuild from '../constructs/PureGuild';
import db from '../providers/database';
import { servers } from '../providers/storage';

export default (message: Message): void => {
  if (!message.member.permissions.has('ADMINISTRATOR')) {
    message.channel.send((mb) => mb.setContent('Only administrators can use this command'));
    return;
  }

  let token: string;
  let output: string;

  if (servers.has(message.guild.id)) {
    const data = servers.get(message.guild.id);

    token = data['token'];
    output = message.channel.id;

    db.run('UPDATE server SET outputChannel = ? WHERE guild = ?', output, message.guild.id);
  } else {
    token = crypto.randomBytes(30).toString('hex');
    output = message.channel.id;
    db.run('INSERT INTO server(guild, token, outputChannel) VALUES(?, ?, ?)', message.guild.id, token, output);
  }

  const guild = new PureGuild(message.guild.id, token, output);

  servers.set(guild.id, guild);

  message.author.openDM().then((channel) => {
    channel.send((mb) =>
      mb.setEmbed((embed: Embed) =>
        embed
          .setColor(0x0099ff)
          .setTitle('Pure Tickets Integration Setup')
          .addField(':ticket:  Your Token', guild.token)
          .addField(':house:  Guild Id', guild.id, true)
          .addField(':office:  Output Channel', guild.output, true),
      ),
    );

    channel.send((mb) =>
      mb.setContent(
        'Keep your guilds token secret. if you need to change the broadcast channel, re-run setup in the desired channel',
      ),
    );
  });

  message.channel.send((mb) => mb.setContent(':envelope: You have been sent a private message with setup information'));
};
