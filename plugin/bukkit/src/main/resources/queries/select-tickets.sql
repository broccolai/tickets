SELECT t.`id`, t.`player`, t.`position`, t.`status`, t.`picker`,
       i.`action`, max(i.`time`) AS `time`, i.`sender`, i.`message`
FROM puretickets_ticket as t
JOIN puretickets_interaction as i ON t.id = i.ticket
WHERE puretickets_ticket.id IN (<ids>)
