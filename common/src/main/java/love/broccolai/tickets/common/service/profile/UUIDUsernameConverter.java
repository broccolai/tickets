package love.broccolai.tickets.common.service.profile;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface UUIDUsernameConverter {

    Optional<String> username(UUID uuid);

    Optional<UUID> uuid(String username);
}
