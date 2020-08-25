import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import { Embed, TextChannel } from '@klasa/core';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { wrap } from '@lib/utilities/strings';
import Action from '@constructs/Action';
import MessageData from '@constructs/MessageData';
import TicketStatus from '@constructs/TicketStatus';
import Ticket from '@constructs/Ticket';
import db from '@lib/providers/database';
import { serialiseLocation } from '@constructs/Location';

const router = express.Router();

router.post('/', async (req, res) => {
  const authReq = req as IBasicAuthedRequest;
  const guild = servers.get(authReq.auth.user);

  res.sendStatus(200);

  const data = req.body as MessageData;
  const channel = (await client.channels.fetch(guild.output)) as TextChannel;
  let ticket: Ticket;

  channel.send((mb) =>
    mb.setEmbed((embed: Embed) => {
      ticket = data.ticket;
      const location = ticket.location;

      return embed
        .setColor(TicketStatus[ticket.status])
        .setAuthor(ticket.player.name, 'https://crafatar.com/avatars/' + ticket.player.uuid)
        .setTitle('' + Action[data.action].title + ' - #' + ticket.id + '')
        .setDescription(
          `\`\`\`YAML
UUID: ${ticket.player.uuid}
World: ${location.world}
Location: X: ${location.x}, Y: ${location.y}, Z: ${location.z}\`\`\``,
        )
        .addField('MESSAGE', wrap(data.ticket.message, 65))
        .setTimestamp(Date.now())
        .setFooter(Action[data.action].author + data.author.name, 'https://crafatar.com/avatars/' + data.author.uuid);
    }),
  );

  db.run('INSERT INTO player(uuid, name) values(?, ?) ON CONFLICT(uuid) DO UPDATE set name = ?', [
    ticket.player.uuid,
    ticket.player.name,
    ticket.player.name,
  ]);

  db.run(
    'INSERT INTO ticket(id, player, location, status, message) values(?, ?, ?, ?, ?) ON CONFLICT(id) DO UPDATE set status = ?, message = ?',
    [
      ticket.id,
      ticket.player,
      serialiseLocation(ticket.location),
      ticket.status,
      ticket.message,
      ticket.status,
      ticket.message,
    ],
  );
});

export default router;
