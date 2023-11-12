package love.broccolai.tickets.core.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketBuilder {

    private final int id;
    private final TicketStatus status;
    private final UUID creator;
    private final Instant date;
    private final UUID assignee;
    private final String message;
    private final List<Action> actions;

    public TicketBuilder(
        final int id,
        final TicketStatus status,
        final UUID creator,
        final Instant date,
        final UUID assignee,
        final String message
    ) {
        this.id = id;
        this.status = status;
        this.creator = creator;
        this.date = date;
        this.assignee = assignee;
        this.message = message;
        this.actions = new ArrayList<>();
    }

    public TicketBuilder withAction(final Action action) {
        this.actions.add(action);
        return this;
    }

    public Ticket build() {
        return new Ticket(
            this.id,
            this.status,
            this.creator,
            this.date,
            this.assignee,
            this.message,
            Collections.unmodifiableList(this.actions)
        );
    }

}
