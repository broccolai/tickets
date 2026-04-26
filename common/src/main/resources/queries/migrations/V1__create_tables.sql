CREATE TABLE tickets_ticket
(
    id         SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE tickets_action
(
    id          SERIAL PRIMARY KEY,
    ticket      INT REFERENCES tickets_ticket (id),
    sequence    INT NOT NULL,
    action_type  VARCHAR(128) NOT NULL,
    actor       UUID NOT NULL,
    occurred_at TIMESTAMP NOT NULL,

    UNIQUE (ticket, sequence)
);

CREATE TABLE tickets_action_property
(
    action        INT REFERENCES tickets_action (id),
    property     VARCHAR(128) NOT NULL,
    value_type   VARCHAR(32) NOT NULL,
    string_value VARCHAR(1024),
    number_value DOUBLE PRECISION,
    boolean_value BOOLEAN,
    uuid_value   UUID,
    instant_value TIMESTAMP,

    PRIMARY KEY (action, property)
);

CREATE TABLE tickets_query_index
(
    ticket          INT REFERENCES tickets_ticket (id),
    type_identifier VARCHAR(128) NOT NULL,
    status          VARCHAR(32) NOT NULL,
    creator         UUID NOT NULL,
    assignee        UUID,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,

    PRIMARY KEY (ticket)
);

CREATE TABLE tickets_profile
(
    uuid     UUID NOT NULL,
    username VARCHAR(255) NOT NULL,

    PRIMARY KEY (uuid)
);
