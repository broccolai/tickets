SELECT t.id, t.player, t.status, t.claimer,
    i.action, i.time, i.sender, i.message,
    c.namespace, c.name, c.value
FROM ticket as t
         LEFT JOIN puretickets_interaction as i
                   ON (t.id = i.ticket)
         LEFT JOIN puretickets_context as c
                   ON (t.id = c.ticket)
WHERE t.id IN (<ids>);
