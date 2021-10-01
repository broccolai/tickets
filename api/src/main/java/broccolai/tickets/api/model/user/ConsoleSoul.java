package broccolai.tickets.api.model.user;

import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class ConsoleSoul extends Soul.Abstract implements OnlineSoul {

    public static final @NonNull UUID UNIQUE_ID = new UUID(0, 0);

    public static final @NonNull String USERNAME = "CONSOLE";

    private final Audience audience;

    public ConsoleSoul(final @NonNull Audience audience) {
        this.audience = audience;
    }

    @Override
    public final @NonNull UUID uuid() {
        return UNIQUE_ID;
    }

    @Override
    public final @NonNull String username() {
        return USERNAME;
    }

    @Override
    public final boolean permission(final @NonNull String permission) {
        return true;
    }

    @Override
    public final @NonNull Audience audience() {
        return this.audience;
    }

}
