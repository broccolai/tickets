CREATE TABLE tickets_ticket
(
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `creator` varchar(36) NOT NULL,
    `creationDate` timestamp,
    `message` varchar(1024)
);
