package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class TicketCommandEvent implements SenderNotificationEvent, StaffNotificationEvent, DiscordNotificationEvent,
        TicketEvent, SoulEvent {

    protected final PlayerSoul soul;
    protected final Ticket ticket;

    public TicketCommandEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    @Override
    public final PlayerSoul soul() {
        return this.soul;
    }

    @Override
    public final Ticket ticket() {
        return this.ticket;
    }

}
