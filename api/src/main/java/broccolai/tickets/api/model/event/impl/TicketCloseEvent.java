package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCloseEvent extends TicketCommandEvent {

    public TicketCloseEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.CLOSE_TICKET, soul, ticket);
    }

}
