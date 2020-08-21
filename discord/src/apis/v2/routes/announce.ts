import express from 'express';
import { IBasicAuthedRequest } from 'express-basic-auth';

import { Embed, TextChannel } from '@klasa/core';

import client from '../../../lib/client';
import { servers } from '../../../lib/providers/storage';
import { wrap } from '../../../lib/utilities/strings';
import Action from '../constructs/Action';
import MessageData from '../constructs/MessageData';
import TicketStatus from '../constructs/TicketStatus';

const router = express.Router();

router.post('/', async (req, res) => {
  const authReq = req as IBasicAuthedRequest;
  const guild = servers.get(authReq.auth.user);

  res.sendStatus(200);

  const data = req.body as MessageData;

  const channel = (await client.channels.fetch(guild.output)) as TextChannel;

  channel.send((mb) =>
    mb.setEmbed((embed: Embed) => {
      const ticket = data.ticket;
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
});

export default router;
