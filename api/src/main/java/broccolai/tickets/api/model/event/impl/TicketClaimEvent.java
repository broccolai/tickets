package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketClaimEvent extends TicketsCommandEvent {

    public TicketClaimEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.CLAIM_TICKET, soul, ticket);
    }

}
