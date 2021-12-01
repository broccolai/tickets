package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineUser;
import broccolai.tickets.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketAssignEvent extends TicketsCommandEvent {

    private final User target;

    public TicketAssignEvent(final @NonNull OnlineUser soul, final @NonNull User target, final @NonNull Ticket ticket) {
        super(NotificationReason.ASSIGN_TICKET, soul, ticket);
        this.target = target;
    }

    public @NonNull User targetSoul() {
        return this.target;
    }

}
