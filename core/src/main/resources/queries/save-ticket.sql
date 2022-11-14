UPDATE tickets_ticket
SET status = :status, assignee = :assignee, message = :message
WHERE id = :id
