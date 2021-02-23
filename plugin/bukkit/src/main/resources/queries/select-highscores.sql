SELECT picker, COUNT(*) AS `num`
FROM puretickets_ticket
WHERE status = :status
  AND picker IS NOT NULL
  and id in (SELECT DISTINCT ticket FROM puretickets_message WHERE date
    > :date)
GROUP BY picker
