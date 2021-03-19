package broccolai.tickets.api.model.user;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class OfflineSoul implements Soul {

    private final UUID uuid;

    public OfflineSoul(final @NonNull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

}
