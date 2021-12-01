package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerUser;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketUpdateEvent extends TicketCommandEvent {

    public TicketUpdateEvent(final @NonNull PlayerUser soul, final @NonNull Ticket ticket) {
        super(NotificationReason.UPDATE_TICKET, soul, ticket);
    }

}
