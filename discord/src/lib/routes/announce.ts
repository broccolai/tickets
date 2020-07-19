import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import { Embed, TextChannel } from '@klasa/core';

import MessageData from '../../constructs/messageData';
import client from '../client';
import { servers } from '../providers/storage';
import { hashToHex } from '../utilities/number';

const router = express.Router();

router.post('/', async (req, res) => {
  const authReq = req as IBasicAuthedRequest;
  const guild = servers.get(authReq.auth.user);

  res.sendStatus(200);

  const data = req.body as MessageData;

  const channel = client.channels.get(guild.output) as TextChannel;

  channel.send((mb) =>
    mb.setEmbed((embed: Embed) => {
      embed
        .setColor(hashToHex(data.color))
        .setAuthor(data.author + ' #' + data.id, 'https://crafatar.com/avatars/' + data.uuid)
        .setTitle('**' + data.action + '**');

      if (data.fields) {
        data.fields.forEach(([key, value]) => {
          embed.addField('**' + key + '**', value, true);
        });
      }

      return embed;
    }),
  );
});

export default router;
