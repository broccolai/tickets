CREATE TABLE tickets_ticket
(
    `id`      serial primary key,
    `creator` varchar(36) not null,
    `date`    timestamp
);

CREATE TABLE tickets_action
(
    `id`     serial,
    `ticket` int references tickets_ticket (id),
    `type`   varchar(36) not null,
    `data`   jsonb       not null,

    PRIMARY KEY (id, ticket)
);
