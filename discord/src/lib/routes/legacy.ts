import express from 'express';

import { Embed, TextChannel } from '@klasa/core';

import client from '../client';
import { servers } from '../providers/storage';
import { hashToHex } from '../utilities/number';

// All routes here are to be removed.
const router = express.Router();

router.post('/announce/:guild/:token', async (req, res) => {
  const guild = req.params.guild;
  const data = servers.get(guild);

  if (data.token == req.params.token) {
    res.send(200);

    const json = req.body;

    const channel = (await client.channels.fetch(data.output)) as TextChannel;

    channel.send((mb) =>
      mb.setEmbed((embed: Embed) => {
        embed
          .setColor(hashToHex(json['color']))
          .setAuthor(
            json['author'] + ' #' + json['id'],
            'https://live.staticflickr.com/7367/10134745566_a7c6dab5bb_z.jpg',
          )
          .setTitle('**' + json['action'] + '**');

        if (json['fields']) {
          Object.entries(json['fields']).forEach(([key, value]) => {
            embed.addField('**' + key + '**', value, true);
          });
        }

        return embed;
      }),
    );
  } else {
    res.send(401);
  }
});

export default router;
