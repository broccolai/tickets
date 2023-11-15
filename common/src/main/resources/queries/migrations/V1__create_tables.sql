CREATE TABLE tickets_ticket
(
    `id`       SERIAL PRIMARY KEY,
    `creator`  varchar(36) NOT NULL,
    `date`     timestamp
);

CREATE TABLE tickets_action
(
    `ticket`   int         NOT NULL,
    `type`     varchar(36) NOT NULL,
    `data`     jsonb       NOT NULL
);
