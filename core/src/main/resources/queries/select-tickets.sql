SELECT t.id, t.creator, t.creationDate, t.assignee, t.message,
FROM tickets_ticket as t
WHERE t.id IN (<ids>);
