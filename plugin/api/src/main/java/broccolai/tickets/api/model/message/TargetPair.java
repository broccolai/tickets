package broccolai.tickets.api.model.message;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import java.util.UUID;

public final class TargetPair {

    private final @NonNull UUID uuid;
    private final @NonNull Component component;

    private TargetPair(final @NonNull UUID uuid, final @NonNull Component component) {
        this.uuid = uuid;
        this.component = component;
    }

    public static TargetPair of(final @NonNull UUID uuid, final @NonNull Component component) {
        return new TargetPair(uuid, component);
    }

    @Pure
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Pure
    public @NonNull Component component() {
        return this.component;
    }

}
