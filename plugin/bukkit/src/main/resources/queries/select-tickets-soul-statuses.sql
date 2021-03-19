SELECT t.*, i.*
FROM puretickets_ticket as t
         LEFT JOIN puretickets_interaction as i
                   ON (t.id = i.ticket) AND i.action = 'MESSAGE'
WHERE i.time = (
    SELECT max(time) FROM puretickets_interaction as i2 WHERE i2.ticket = t.id AND i2.action = 'MESSAGE'
)
AND t.player = :player
AND t.status IN (<statuses>)
