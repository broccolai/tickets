CREATE TABLE tickets_ticket
(
    `id`       int         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `creator`  varchar(36) NOT NULL,
    `date`     timestamp
);

CREATE TABLE tickets_action
(
    `type`     varchar(36) NOT NULL,
    `ticket`   int         NOT NULL AUTO_INCREMENT,
    `creator`  varchar(36) NOT NULL,
    `date`     timestamp   NOT NULL,
    `message`  varchar(1024) NULL,
    `assignee` varchar(36) NULL
);
