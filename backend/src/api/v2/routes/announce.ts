import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { wrap } from '@lib/utilities/strings';
import Action from '@constructs/Action';
import MessageData from '@constructs/MessageData';
import TicketStatus from '@constructs/TicketStatus';
import db from '@lib/providers/database';
import { serialiseLocation } from '@constructs/Location';
import { EmbedBuilder, TextChannel } from 'discord.js';

const router = express.Router();

router.post('/', async (req, res) => {
  res.sendStatus(200);

  const authReq = req as IBasicAuthedRequest;
  const { output } = servers.get(authReq.auth.user);
  const { server, ticket, author, action, note } = req.body as MessageData;
  const { id, player, location, status, message } = ticket;
  const channel = (await client.channels.fetch(output)) as TextChannel;

  const serverText = server != null ? 'Server: ' + server : '';

  const embed = new EmbedBuilder()
    .setColor(TicketStatus[status])
    .setAuthor({ name: player.name, iconURL: 'https://crafatar.com/avatars/' + player.uuid })
    .setTitle(Action[action].title + ' - #' + id)
    .setDescription(
      `\`\`\`YAML
${serverText}
UUID: ${player.uuid}
World: ${location.world}
Location: X: ${location.x}, Y: ${location.y}, Z: ${location.z}\`\`\``,
    );

  const fields = [];

  if (note != null) {
    fields.push({ name: 'LAST NOTE', value: note });
  }

  fields.push({ name: 'MESSAGE', value: wrap(message, 65) });

  embed
    .addFields(fields)
    .setTimestamp(Date.now())
    .setFooter({ text: Action[action].author + author.name, iconURL: 'https://crafatar.com/avatars/' + author.uuid });

  channel.send({ embeds: [embed] });

  db.run('INSERT INTO player(uuid, name) values(?, ?) ON CONFLICT(uuid) DO UPDATE set name = ?', [player.uuid, player.name, player.name]);

  db.run(
    'INSERT INTO ticket(guild, id, player, location, status, note, message) values(?, ?, ?, ?, ?, ?, ?) ON CONFLICT(guild, id) DO UPDATE set status = ?, note = ?, message = ?',
    [authReq.auth.user, id, player.uuid, serialiseLocation(location), status, note, message, status, note, message],
  );
});

export default router;
