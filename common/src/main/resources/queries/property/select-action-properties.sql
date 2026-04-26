SELECT property, value_type, string_value, number_value, boolean_value, uuid_value, instant_value
FROM tickets_action_property
WHERE action = :action
ORDER BY property;
