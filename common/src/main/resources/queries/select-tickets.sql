SELECT t.id,
       t.creator,
       t.date,
       a.type,
       a.data
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE t.id IN (<ids>)
ORDER BY a.id;
