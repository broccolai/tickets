package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketUpdateEvent extends TicketCommandEvent {

    public TicketUpdateEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.UPDATE_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        Component component = messageService.senderTicketUpdate(this.ticket);
        this.soul.sendMessage(component);
    }

    @Override
    public @NonNull Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketUpdate(this.ticket);
    }

}
