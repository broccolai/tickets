INSERT INTO tickets_action(`type`, `ticket`, `creator`, `date`, `message`)
VALUES (:type, :ticket, :creator, :date, :message);
SELECT LAST_INSERT_ID();
