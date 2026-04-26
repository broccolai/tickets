INSERT INTO tickets_query_index(
    ticket,
    type_identifier,
    status,
    creator,
    assignee,
    created_at,
    updated_at
)
VALUES (
    :ticket,
    :type_identifier,
    :status,
    :creator,
    :assignee,
    :created_at,
    :updated_at
);
