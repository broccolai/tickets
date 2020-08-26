import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import { Embed, TextChannel } from '@klasa/core';

import client from '@lib/client';
import { servers } from '@lib/providers/storage';
import { wrap } from '@lib/utilities/strings';
import Action from '@constructs/Action';
import MessageData from '@constructs/MessageData';
import TicketStatus from '@constructs/TicketStatus';
import db from '@lib/providers/database';
import { serialiseLocation } from '@constructs/Location';

const router = express.Router();

router.post('/', async (req, res) => {
  res.sendStatus(200);

  const authReq = req as IBasicAuthedRequest;
  const { output } = servers.get(authReq.auth.user);
  const { ticket, author, action } = req.body as MessageData;
  const { id, player, location, status, message } = ticket;
  const channel = (await client.channels.fetch(output)) as TextChannel;

  await channel.send((mb) =>
    mb.setEmbed((embed: Embed) => {
      return embed
        .setColor(TicketStatus[status])
        .setAuthor(player.name, 'https://crafatar.com/avatars/' + player.uuid)
        .setTitle(Action[action].title + ' - #' + id)
        .setDescription(
          `\`\`\`YAML
UUID: ${player.uuid}
World: ${location.world}
Location: X: ${location.x}, Y: ${location.y}, Z: ${location.z}\`\`\``,
        )
        .addField('MESSAGE', wrap(message, 65))
        .setTimestamp(Date.now())
        .setFooter(Action[action].author + author.name, 'https://crafatar.com/avatars/' + author.uuid);
    }),
  );

  db.run('INSERT INTO player(uuid, name) values(?, ?) ON CONFLICT(uuid) DO UPDATE set name = ?', [player.uuid, player.name, player.name]);

  db.run(
    'INSERT INTO ticket(guild, id, player, location, status, message) values(?, ?, ?, ?, ?, ?) ON CONFLICT(guild, id) DO UPDATE set status = ?, message = ?',
    [authReq.auth.user, id, player.uuid, serialiseLocation(location), status, message, status, message],
  );
});

export default router;
