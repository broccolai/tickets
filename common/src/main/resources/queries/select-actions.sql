SELECT id, ticket, action_type, actor, occurred_at
FROM tickets_action
WHERE ticket = :ticket
ORDER BY sequence;
