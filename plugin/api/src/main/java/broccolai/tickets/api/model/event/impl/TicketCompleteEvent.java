package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCompleteEvent extends TicketsCommandEvent {

    public TicketCompleteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.DONE_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        this.soul.sendMessage(oldMessageService.senderTicketComplete(this.ticket));
    }

    @Override
    public @NonNull TargetPair target(final @NonNull OldMessageService oldMessageService) {
        return new TargetPair(this.ticket.player(), oldMessageService.targetTicketComplete(this.ticket, this.soul));
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketComplete(this.ticket, this.soul);
    }

}
