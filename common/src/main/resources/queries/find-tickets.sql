SELECT ticket
FROM tickets_query_index
WHERE status IN (<statuses>)
    AND (:creator IS NULL OR creator = :creator)
    AND (:since IS NULL OR created_at > :since)
ORDER BY ticket;
