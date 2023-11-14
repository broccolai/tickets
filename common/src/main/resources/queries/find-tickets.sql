SELECT t.id,
       t.creator,
       t.date,
       a.type,
       a.creator  as action_creator,
       a.date     as action_date,
       a.message  as action_message,
       a.assignee as action_assignee
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE (:since IS NULL OR t.date > :since);
