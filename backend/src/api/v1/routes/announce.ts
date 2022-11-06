import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { hashToHex } from '@lib/utilities/number';
import { EmbedBuilder, TextChannel } from 'discord.js';

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

  const embed = new EmbedBuilder()
    .setColor(hashToHex(data.color))
    .setAuthor({ name: data.author + ' #' + data.id, iconURL: 'https://crafatar.com/avatars/' + data.uuid })
    .setTitle('**' + data.action + '**');

  if (data.fields) {
    const fields = Object.entries(data.fields).map(([key, value]) => {
      return { name: '**' + key + '**', value: value, inline: true };
    });

    embed.addFields(fields);
  }

  channel.send({ embeds: [embed] });
});

export default router;
