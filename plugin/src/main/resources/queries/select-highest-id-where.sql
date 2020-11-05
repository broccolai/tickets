SELECT max(id) AS 'id'
FROM puretickets_ticket
WHERE uuid = :uuid
AND status IN (<statuses>)
