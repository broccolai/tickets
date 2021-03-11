INSERT INTO puretickets_ticket(`player`, `position`, `status`, `picker`)
VALUES (:player, :position, :status, :picker);
SELECT LAST_INSERT_ID();
INSERT INTO puretickets_interaction(`ticket`, `action`, `time`, `sender`, `message`)
VALUES (:ticket, :action, :time, :sender, :message);
