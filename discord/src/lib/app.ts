import express from 'express';
import client from './client';
import { TextChannel } from 'discord.js';
import { servers } from './storage';

const app = express();

app.use(express.json());

app.post('/announce/:guild/:token', async (req, res) => {
  const guild = req.params.guild;
  const data = servers.get(guild);

  if (data.token == req.params.token) {
    res.send(200);

    const channel = client.channels.cache.get(data.output) as TextChannel;
    channel.send(req.body.data);
  } else {
    res.send(401);
  }
});

export default app;
