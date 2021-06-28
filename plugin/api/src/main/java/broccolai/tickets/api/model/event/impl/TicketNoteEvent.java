package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketNoteEvent extends TicketsCommandEvent {

    private final String note;

    public TicketNoteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket, final @NonNull String note) {
        super(NotificationReason.NOTE_TICKET, soul, ticket);
        this.note = note;
    }

    public @NonNull String note() {
        return this.note;
    }

}
