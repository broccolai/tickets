SELECT claimer, COUNT(*) AS `num`
FROM puretickets_ticket
WHERE status = 'CLOSED'
  AND claimer IS NOT NULL
  AND id IN (SELECT DISTINCT ticket FROM puretickets_interaction WHERE `time` > :time)
GROUP BY claimer
