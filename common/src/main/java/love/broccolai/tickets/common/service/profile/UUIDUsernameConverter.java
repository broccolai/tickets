package love.broccolai.tickets.common.service.profile;

import java.util.UUID;

public interface UUIDUsernameConverter {

    String username(UUID uuid);

    UUID uuid(String username);
}
