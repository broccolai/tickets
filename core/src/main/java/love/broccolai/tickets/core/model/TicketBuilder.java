package love.broccolai.tickets.core.model;

import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TicketBuilder {

    private final int id;
    private final TicketStatus status;
    private final UUID creator;
    private final Instant date;
    private final UUID assignee;
    private final String message;
    private final Set<Action> actions;

    public TicketBuilder(
            final int id,
            final @NonNull TicketStatus status,
            final @NonNull UUID creator,
            final @NonNull Instant date,
            final @NonNull UUID assignee,
            final @NonNull String message
    ) {
        this.id = id;
        this.status = status;
        this.creator = creator;
        this.date = date;
        this.assignee = assignee;
        this.message = message;
        this.actions = new HashSet<>();
    }

    public @This TicketBuilder withAction(final @NonNull Action action) {
        this.actions.add(action);
        return this;
    }

    public @NonNull Ticket build() {
        return new Ticket(
                this.id,
                this.status,
                this.creator,
                this.date,
                this.assignee,
                this.message,
                Collections.unmodifiableSet(this.actions)
        );
    }

}
