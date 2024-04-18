SELECT t.id,
       t.type_identifier,
       t.creator,
       t.date,
       a.type,
       a.data
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE t.creator = COALESCE(:creator, t.creator)
   OR t.date > COALESCE(:since, '1900-01-01'::DATE)
ORDER BY a.id;
