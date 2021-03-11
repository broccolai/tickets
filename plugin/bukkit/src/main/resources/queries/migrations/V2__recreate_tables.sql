DROP TABLE IF EXISTS puretickets_ticket;
DROP TABLE IF EXISTS puretickets_message;
DROP TABLE IF EXISTS puretickets_notification;
DROP TABLE IF EXISTS puretickets_settings;

CREATE TABLE IF NOT EXISTS puretickets_ticket
(
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `player` varchar(36) NOT NULL,
    `position` varchar(255),
    `status` varchar(255) NOT NULl,
    `picker` varchar(36) NULl
);

CREATE TABLE IF NOT EXISTS puretickets_interaction
(
    `ticket` int NOT NULL,
    `action` varchar(36) NOT NULL,
    `time` DATETIME NOT NULL,
    `sender` varchar(36) NOT NULL,
    `message` varchar(255) NULl,
    PRIMARY KEY(`ticket`, `action`, `time`)
);

CREATE TABLE IF NOT EXISTS puretickets_notification
(
    `uuid` varchar(36) NOT NULL,
    `message` varchar(255) NOT NULL,
    `replacements` varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS puretickets_settings
(
    `uuid` varchar(36) NOT NULL,
    `announcements` BOOLEAN NOT NULL
);
