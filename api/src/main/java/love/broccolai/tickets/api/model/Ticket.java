package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Ticket {

    private final int id;
    private TicketStatus status;
    private final @NonNull UUID creator;
    private final @NonNull Instant date;
    private @Nullable UUID assignee;
    private @NonNull String message;
    private final @NonNull List<Action> actions;

    public Ticket(
            final int id,
            final TicketStatus status,
            final @NonNull UUID creator,
            final @NonNull Instant date,
            final @Nullable UUID assignee,
            final @NonNull String message,
            final @NonNull List<Action> actions
    ) {
        this.id = id;
        this.status = status;
        this.creator = creator;
        this.date = date.truncatedTo(ChronoUnit.SECONDS);
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

    public @NonNull TicketStatus status() {
        return this.status;
    }

    public void status(final @NonNull TicketStatus status) {
        this.status = status;
    }

    public @NonNull Instant date() {
        return this.date;
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

    public @NonNull List<Action> actions() {
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
                && Objects.equals(this.date, that.date)
                && Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.creator, this.date, this.message);
    }

}
