import cors from 'cors';
import { MessageEmbed, TextChannel } from 'discord.js';
import express from 'express';

import client from './client';
import { servers } from './storage';

const app = express();

app.use(express.json());
app.use(cors());

app.post('/announce/:guild/:token', async (req, res) => {
  const guild = req.params.guild;
  const data = servers.get(guild);

  if (data.token == req.params.token) {
    res.send(200);

    const json = req.body;

    const channel = client.channels.cache.get(data.output) as TextChannel;
    const message = new MessageEmbed()
      .setColor(json['color'])
      .setAuthor(json['author'] + ' #' + json['id'], 'https://live.staticflickr.com/7367/10134745566_a7c6dab5bb_z.jpg')
      .setTitle('**' + json['action'] + '**');

    if (json['fields']) {
      Object.entries(json['fields']).forEach(([key, value]) => {
        message.addField('**' + key + '**', value, true);
      });
    }

    channel.send(message);
  } else {
    res.send(401);
  }
});

app.get('/status', async (_req, res) => {
  res.send(client.guilds.cache.size.toString());
});

export default app;
