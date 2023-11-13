INSERT INTO tickets_ticket(`status`, `creator`, `date`, `message`)
VALUES ('OPEN', :creator, :date, :message);
SELECT LAST_INSERT_ID();
