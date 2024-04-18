INSERT INTO tickets_ticket(type_identifier, creator, date)
VALUES (:type_identifier, :creator, :date);

INSERT INTO tickets_action(ticket, type, data)
VALUES (:id, 'open', :data FORMAT JSON);
