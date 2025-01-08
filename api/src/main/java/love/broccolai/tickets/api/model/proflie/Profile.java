package love.broccolai.tickets.api.model.proflie;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Profile {
    private final UUID uuid;
    private String username;

    public Profile(
        UUID uuid,
        String username
    ) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public String username() {
        return this.username;
    }

    public void username(String username) {
        this.username = username;
    }

}
