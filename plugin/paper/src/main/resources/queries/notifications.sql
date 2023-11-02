SELECT message FROM puretickets_notification
WHERE uuid = :uuid;
DELETE FROM puretickets_notification WHERE uuid = :uuid;
