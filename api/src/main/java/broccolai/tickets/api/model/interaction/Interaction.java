package broccolai.tickets.api.model.interaction;

import java.time.LocalDateTime;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Interaction {

    private final Action action;
    private final LocalDateTime time;
    private final UUID causer;

    public Interaction(
            final @NonNull Action action,
            final @NonNull LocalDateTime time,
            final @NonNull UUID causer
    ) {
        this.action = action;
        this.time = time;
        this.causer = causer;
    }

    public final @NonNull Action action() {
        return this.action;
    }

    public final @NonNull LocalDateTime time() {
        return this.time;
    }

    public final @NonNull UUID sender() {
        return this.causer;
    }

}
