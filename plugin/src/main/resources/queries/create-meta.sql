CREATE TABLE IF NOT EXISTS puretickets_meta(version INTEGER);
INSERT INTO puretickets_meta(version)
SELECT 1
WHERE NOT EXISTS (SELECT * FROM puretickets_meta);
