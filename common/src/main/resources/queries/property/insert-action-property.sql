INSERT INTO tickets_action_property(
    action,
    property,
    value_type,
    string_value,
    number_value,
    boolean_value,
    uuid_value,
    instant_value
)
VALUES (
    :action,
    :property,
    :value_type,
    :string_value,
    :number_value,
    :boolean_value,
    :uuid_value,
    :instant_value
);
