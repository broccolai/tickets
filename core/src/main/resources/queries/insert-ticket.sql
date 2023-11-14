INSERT INTO tickets_ticket(`creator`, `date`)
VALUES (:creator, :date);
SELECT LAST_INSERT_ID();
