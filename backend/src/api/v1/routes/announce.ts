import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import { Embed, TextChannel } from '@klasa/core';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { hashToHex } from '@lib/utilities/number';

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

  channel.send((mb) =>
    mb.setEmbed((embed: Embed) => {
      embed
        .setColor(hashToHex(data.color))
        .setAuthor(data.author + ' #' + data.id, 'https://crafatar.com/avatars/' + data.uuid)
        .setTitle('**' + data.action + '**');

      if (data.fields) {
        Object.entries(data.fields).forEach(([key, value]) => {
          embed.addField('**' + key + '**', value, true);
        });
      }

      return embed;
    }),
  );
});

export default router;
