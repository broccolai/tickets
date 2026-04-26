SELECT t.id
FROM tickets_ticket AS t
WHERE t.id IN (<ids>)
ORDER BY t.id;
