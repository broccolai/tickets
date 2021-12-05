INSERT INTO tickets_ticket(`creator`, `date`, `message`)
VALUES (:creator, :date, :message);
SELECT LAST_INSERT_ID();
