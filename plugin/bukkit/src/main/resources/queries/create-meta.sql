CREATE TABLE IF NOT EXISTS puretickets_meta
(
    info INTEGER PRIMARY KEY DEFAULT 0,
    version INTEGER
);
INSERT INTO puretickets_meta(version) VALUES(0);
SELECT 1 WHERE NOT EXISTS (SELECT * FROM puretickets_meta);
