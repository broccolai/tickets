INSERT INTO puretickets_ticket(`player`, `position`, `status`, `claimer`)
VALUES (:player, :position, :status, :claimer);
SELECT LAST_INSERT_ID();
INSERT INTO puretickets_interaction(`ticket`, `action`, `time`, `sender`, `message`)
VALUES (:ticket, :action, :time, :sender, :message);
