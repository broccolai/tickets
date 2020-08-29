import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { hashToHex } from '@lib/utilities/number';
import { MessageEmbed, TextChannel } from 'discord.js';

const router = express.Router();

type MessageData = {
  id: string;
  uuid: string;
  author: string;
  color: string;
  action: string;
  fields?: {
    [key: string]: string;
  };
};

router.post('/', async (req, res) => {
  const authReq = req as IBasicAuthedRequest;
  const guild = servers.get(authReq.auth.user);

  res.sendStatus(200);

  const data = req.body as MessageData;
  const channel = (await client.channels.fetch(guild.output)) as TextChannel;

  const embed = new MessageEmbed()
    .setColor(hashToHex(data.color))
    .setAuthor(data.author + ' #' + data.id, 'https://crafatar.com/avatars/' + data.uuid)
    .setTitle('**' + data.action + '**');

  if (data.fields) {
    Object.entries(data.fields).forEach(([key, value]) => {
      embed.addField('**' + key + '**', value, true);
    });
  }

  channel.send(embed);
});

export default router;
