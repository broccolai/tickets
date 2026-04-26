package love.broccolai.tickets.api.model.profile;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Profile(
    UUID uuid,
    String username
) {

    public Profile withUsername(final String username) {
        return new Profile(this.uuid, username);
    }
}
