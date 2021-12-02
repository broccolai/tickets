INSERT INTO tickets_ticket(`creator`, `creationDate`, `message`)
VALUES (:creator, :creationDate, :message);
SELECT LAST_INSERT_ID();
