package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCompleteEvent extends TicketsCommandEvent {

    public TicketCompleteEvent(final @NonNull OnlineUser soul, final @NonNull Ticket ticket) {
        super(NotificationReason.DONE_TICKET, soul, ticket);
    }

}
