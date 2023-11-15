INSERT INTO tickets_ticket(`creator`, `date`)
VALUES (:creator, :date);

INSERT INTO tickets_action(`ticket`, `type`, `data`)
VALUES (LASTVAL(), 'open', :data);
