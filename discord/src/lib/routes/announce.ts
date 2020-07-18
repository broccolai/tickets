import { MessageEmbed, TextChannel } from 'discord.js';
import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import MessageData from '../../constructs/messageData';
import client from '../client';
import { servers } from '../providers/storage';

const router = express.Router();

router.post('/', async (req, res) => {
  const authReq = req as IBasicAuthedRequest;
  const guild = servers.get(authReq.auth.user);

  res.sendStatus(200);

  const data = req.body as MessageData;

  const channel = client.channels.cache.get(guild.output) as TextChannel;
  const message = new MessageEmbed()
    .setColor(data.color)
    .setAuthor(data.author + ' #' + data.id, 'https://live.staticflickr.com/7367/10134745566_a7c6dab5bb_z.jpg')
    .setTitle('**' + data.action + '**');

  if (data.fields) {
    data.fields.forEach(([key, value]) => {
      message.addField('**' + key + '**', value, true);
    });
  }

  channel.send(message);
});

export default router;
