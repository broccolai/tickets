package broccolai.tickets.api.model.user;

import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SoulSnapshot extends Soul.Abstract {

    private final UUID uuid;
    private final String username;

    public SoulSnapshot(final @NonNull UUID uuid, final @NonNull String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Override
    public @NonNull String username() {
        return this.username;
    }

}
