INSERT INTO tickets_action(ticket, type_identifier, data)
VALUES (:ticket, :type_identifier, :data FORMAT JSON);
