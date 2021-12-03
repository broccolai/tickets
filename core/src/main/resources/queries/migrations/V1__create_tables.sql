CREATE TABLE tickets_ticket
(
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `creator` varchar(36) NOT NULL,
    `creationDate` timestamp,
    `assignee` varchar(36),
    `message` varchar(1024)
);

CREATE TABLE tickets_action
(
    `ticket` int NOT NULL AUTO_INCREMENT,
    `creator` varchar(36) NOT NULL,
    `creationDate` timestamp NOT NULL,
    `message` varchar(1024) NOT NULL
);
