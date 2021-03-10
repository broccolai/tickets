SELECT `id`, `player`, `position`, `status`, `picker`, `action`, max(`time`), `sender`, `message`
FROM puretickets_ticket
JOIN puretickets_interaction
WHERE puretickets_ticket.id = :id
