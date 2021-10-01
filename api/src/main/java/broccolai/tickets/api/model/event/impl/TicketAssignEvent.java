package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketAssignEvent extends TicketsCommandEvent {

    private final Soul target;

    public TicketAssignEvent(final @NonNull OnlineSoul soul, final @NonNull Soul target, final @NonNull Ticket ticket) {
        super(NotificationReason.ASSIGN_TICKET, soul, ticket);
        this.target = target;
    }

    public @NonNull Soul targetSoul() {
        return this.target;
    }

}
