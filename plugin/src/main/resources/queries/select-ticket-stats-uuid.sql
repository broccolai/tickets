SELECT
SUM(Status LIKE 'OPEN') AS open,
SUM(Status LIKE 'PICKED') AS picked,
SUM(status LIKE 'CLOSED') AS closed
FROM puretickets_ticket
WHERE uuid = :uuid;
