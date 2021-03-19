SELECT picker, COUNT(*) AS `num`
FROM puretickets_ticket
WHERE status = 'CLOSED'
AND picker IS NOT NULL
AND id IN (SELECT DISTINCT ticket FROM puretickets_message WHERE date > :date)
GROUP BY picker
