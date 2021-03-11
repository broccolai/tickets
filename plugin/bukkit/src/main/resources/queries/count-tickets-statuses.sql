SELECT COUNT(`id`) AS amount
FROM puretickets_ticket
WHERE puretickets_ticket.status IN (<statuses>)
