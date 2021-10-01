CREATE TABLE IF NOT EXISTS puretickets_ticket
(
    id
    INTEGER,
    uuid
    TEXT,
    status
    TEXT,
    picker
    TEXT,
    location
    TEXT
);
CREATE TABLE IF NOT EXISTS puretickets_message
(
    ticket
    INTEGER,
    reason
    TEXT,
    data
    TEXT,
    sender
    TEXT,
    date
    TEXT
);
CREATE TABLE IF NOT EXISTS puretickets_notification
(
    uuid
    TEXT,
    message
    TEXT,
    replacements
    TEXT
);
CREATE TABLE IF NOT EXISTS puretickets_settings
(
    uuid
    TEXT,
    announcements
    TEXT
);
