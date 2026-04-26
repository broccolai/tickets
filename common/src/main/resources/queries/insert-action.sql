INSERT INTO tickets_action(ticket, sequence, action_type, actor, occurred_at)
VALUES (
    :ticket,
    (SELECT COALESCE(MAX(sequence), -1) + 1 FROM tickets_action WHERE ticket = :ticket),
    :action_type,
    :actor,
    :occurred_at
);
