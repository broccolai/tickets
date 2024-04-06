CREATE TABLE tickets_ticket
(
    id              serial primary key,
    type_identifier varchar(36) not null,
    creator         uuid not null,
    date            timestamp
);

CREATE TABLE tickets_action
(
    id     serial,
    ticket int references tickets_ticket (id),
    type   varchar(36) not null,
    data   jsonb       not null,

    PRIMARY KEY (id, ticket)
);
