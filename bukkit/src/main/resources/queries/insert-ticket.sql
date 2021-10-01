INSERT INTO puretickets_ticket(`player`, `status`, `claimer`)
VALUES (:player, :status, :claimer);
SELECT LAST_INSERT_ID();
INSERT INTO puretickets_interaction(`ticket`, `action`, `time`, `sender`, `message`)
VALUES (:ticket, :action, :time, :sender, :message);
