package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCloseEvent extends TicketCommandEvent {

    public TicketCloseEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.CLOSE_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        this.soul.sendMessage(oldMessageService.senderTicketClose(this.ticket));
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketClose(this.ticket);
    }

}
