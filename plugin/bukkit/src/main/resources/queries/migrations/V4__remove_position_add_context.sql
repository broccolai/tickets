ALTER TABLE puretickets_ticket
    DROP COLUMN position;

CREATE TABLE IF NOT EXISTS puretickets_context
(
    `ticket` int NOT NULL,
    `namespace` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `value` LONGTEXT,

    PRIMARY KEY (ticket, namespace, name)
);
