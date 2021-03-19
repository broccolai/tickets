UPDATE puretickets_ticket
SET `status` = :status, claimer = :claimer
WHERE id = :id
