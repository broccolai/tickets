SELECT COUNT(id)
FROM puretickets_ticket
WHERE uuid = :uuid
  AND status IN (<statuses>);
