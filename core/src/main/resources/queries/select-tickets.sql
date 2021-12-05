SELECT t.id, t.creator, t.date, t.assignee, t.message,
       a.type, a.creator, a.date, a.message,
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE t.id IN (<ids>);
