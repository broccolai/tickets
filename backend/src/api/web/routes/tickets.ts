import express from 'express';
import db from '@lib/providers/database';
import { ticketFromRow } from '@constructs/Ticket';

const router = express.Router();

router.get('/', async (req, res) => {
  // const data = req.body as RequestData;

  db.all('SELECT * FROM ticket', async (err, rows) => {
    if (err) {
      res.sendStatus(500);
      return;
    }

    const tickets = [];

    for (const row of rows) {
      tickets.push(await ticketFromRow(row as never));
    }

    res.json({ tickets: tickets });
  });
});

// type RequestData = {
//   guild: number;
// };

export default router;
