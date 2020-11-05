SELECT id, uuid, status, picker, location,
	   ticket, reason, data, sender, date
FROM puretickets_ticket
LEFT JOIN puretickets_message ON puretickets_ticket.id = puretickets_message.ticket
WHERE puretickets_ticket.id IN (<ids>);
