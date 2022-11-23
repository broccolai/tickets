SELECT t.id, t.status, t.creator, t.date, t.assignee, t.message,
       a.type, a.creator as action_creator, a.date as action_date, a.message as action_message, a.assignee as action_assignee
FROM tickets_ticket as t
         LEFT JOIN tickets_action as a
                   ON (t.id = a.ticket)
WHERE t.status = :status
  AND (:assignee IS NULL OR t.assignee = :assignee)
  AND (:since IS NULL OR t.date > :since);
