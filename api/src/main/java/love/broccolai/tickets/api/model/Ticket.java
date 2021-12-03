package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;

public final class Ticket {

    private final int id;
    private final @NonNull UUID creator;
    private final @NonNull Instant creationDate;
    private @Nullable UUID assignee;
    private @NonNull String message;
    private final @NonNull Set<Action> actions;

    public Ticket(
            final int id,
            final @NonNull UUID creator,
            final @NonNull Instant creationDate,
            final @Nullable UUID assignee,
            final @NonNull String message,
            final @NonNull Set<Action> actions
    ) {
        this.id = id;
        this.creator = creator;
        this.creationDate = creationDate.truncatedTo(ChronoUnit.SECONDS);
        this.assignee = assignee;
        this.message = message;
        this.actions = actions;
    }

    public int id() {
        return this.id;
    }

    public @NonNull UUID creator() {
        return this.creator;
    }

    public @NonNull Instant creationDate() {
        return this.creationDate;
    }

    public @NonNull Optional<@NonNull UUID> assignee() {
        return Optional.ofNullable(this.assignee);
    }

    public void assignee(final @NonNull UUID assignee) {
        this.assignee = assignee;
    }

    public @NonNull String message() {
        return this.message;
    }

    public void message(final @NonNull String message) {
        this.message = message;
    }

    public @NonNull Set<Action> actions() {
        return this.actions;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Ticket that)) {
            return false;
        }

        return this.id == that.id
                && Objects.equals(this.creator, that.creator)
                && Objects.equals(this.creationDate, that.creationDate)
                && Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.creator, this.creationDate, this.message);
    }

    public static final class Builder {

        private final int id;
        private final UUID creator;
        private final Instant creationDate;
        private final UUID assignee;
        private final String message;
        private final Set<Action> actions;

        public Builder(
                final int id,
                final @NonNull UUID creator,
                final @NonNull Instant creationDate,
                final @NonNull UUID assignee,
                final @NonNull String message
        ) {
            this.id = id;
            this.creator = creator;
            this.creationDate = creationDate;
            this.assignee = assignee;
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
                    this.assignee,
                    this.message,
                    Collections.unmodifiableSet(this.actions)
            );
        }

    }

}
