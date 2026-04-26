SELECT uuid, username
FROM tickets_profile
WHERE LOWER(username) = LOWER(:username);
