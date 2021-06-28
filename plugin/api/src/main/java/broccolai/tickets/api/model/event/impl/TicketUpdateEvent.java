package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketUpdateEvent extends TicketCommandEvent {

    public TicketUpdateEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.UPDATE_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        Component component = oldMessageService.senderTicketUpdate(this.ticket);
        this.soul.sendMessage(component);
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketUpdate(this.ticket);
    }

}
