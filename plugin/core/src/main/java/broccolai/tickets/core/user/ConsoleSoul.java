package broccolai.tickets.core.user;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public abstract class ConsoleSoul<C> implements Soul<C> {

    public static final UUID CONSOLE_UUID = new UUID(0L, 0L);

    /**
     * Construct a ConsoleSoul
     */
    public ConsoleSoul() {
    }

    @Override
    public final @NonNull String getName() {
        return "CONSOLE";
    }

    @Override
    public final @NonNull UUID getUniqueId() {
        return CONSOLE_UUID;
    }

}
