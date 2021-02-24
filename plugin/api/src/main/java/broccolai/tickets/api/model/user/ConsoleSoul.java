package broccolai.tickets.api.model.user;

import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class ConsoleSoul implements OnlineSoul {

    public static final @NonNull UUID UUID = new UUID(0, 0);

    public static final @NonNull String USERNAME = "CONSOLE";

    private final Audience audience;

    public ConsoleSoul(final @NonNull Audience audience) {
        this.audience = audience;
    }

    @Override
    public @NonNull UUID uuid() {
        return UUID;
    }

    @Override
    public @NonNull String username() {
        return USERNAME;
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

}
