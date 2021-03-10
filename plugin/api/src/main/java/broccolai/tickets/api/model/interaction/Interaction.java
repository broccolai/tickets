package broccolai.tickets.api.model.interaction;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Interaction implements Comparable<Interaction> {

    private final Action action;
    private final LocalDateTime time;
    private final UUID sender;

    public Interaction(
            final @NonNull Action action,
            final @NonNull LocalDateTime time,
            final @NonNull UUID sender
    ) {
        this.action = action;
        this.time = time;
        this.sender = sender;
    }

    public final @NonNull Action action() {
        return this.action;
    }

    public final @NonNull LocalDateTime time() {
        return this.time;
    }

    public final @NonNull UUID sender() {
        return this.sender;
    }

    @Override
    public final int compareTo(@NonNull final Interaction interaction) {
        return this.time.compareTo(interaction.time);
    }

}
