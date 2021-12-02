package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

public record Ticket(
        int id,
        @NonNull UUID creator,
        @NonNull Instant creationDate,
        @NonNull String message,
        @NonNull Set<Action> actions
) {

    public @NonNull Ticket withMessage(final @NonNull String message) {
        return new Ticket(this.id, this.creator, this.creationDate, message, this.actions);
    }

    public static final class Builder {

        private final int id;
        private final UUID creator;
        private final Instant creationDate;
        private final String message;
        private final Set<Action> actions;

        public Builder(
                final int id,
                final @NonNull UUID creator,
                final @NonNull Instant creationDate,
                final @NonNull String message
        ) {
            this.id = id;
            this.creator = creator;
            this.creationDate = creationDate;
            this.message = message;
            this.actions = new HashSet<>();
        }

        public @This Builder withAction(final @NonNull Action action) {
            this.actions.add(action);
            return this;
        }

        public @NonNull Ticket build() {
            return new Ticket(
                    this.id,
                    this.creator,
                    this.creationDate,
                    this.message,
                    Collections.unmodifiableSet(this.actions)
            );
        }

    }

}
