package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class TicketsCommandEvent implements SenderNotificationEvent, TargetNotificationEvent, StaffNotificationEvent,
        DiscordNotificationEvent, TicketEvent, SoulEvent {

    protected final OnlineSoul soul;
    protected final Ticket ticket;

    public TicketsCommandEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    @Override
    public final Soul soul() {
        return this.soul;
    }

    @Override
    public final Ticket ticket() {
        return this.ticket;
    }

}
