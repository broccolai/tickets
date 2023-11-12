package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class Ticket {

    private final int id;
    private TicketStatus status;
    private final UUID creator;
    private final Instant date;
    private @Nullable UUID assignee;
    private String message;
    private final List<Action> actions;

    public Ticket(
        final int id,
        final TicketStatus status,
        final UUID creator,
        final Instant date,
        final @Nullable UUID assignee,
        final String message,
        final List<Action> actions
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

    public UUID creator() {
        return this.creator;
    }

    public TicketStatus status() {
        return this.status;
    }

    public void status(final TicketStatus status) {
        this.status = status;
    }

    public Instant date() {
        return this.date;
    }

    public Optional<UUID> assignee() {
        return Optional.ofNullable(this.assignee);
    }

    public void assignee(final UUID assignee) {
        this.assignee = assignee;
    }

    public String message() {
        return this.message;
    }

    public void message(final String message) {
        this.message = message;
    }

    public List<Action> actions() {
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
