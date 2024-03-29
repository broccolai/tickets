SELECT t.id,
       t.creator,
       t.date,
       a.type,
       a.data
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE (:creator IS NULL OR t.creator = :creator)
   OR (:since IS NULL OR t.date > :since)
ORDER BY a.id;
