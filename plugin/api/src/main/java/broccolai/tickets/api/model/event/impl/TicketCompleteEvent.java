package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCompleteEvent extends TicketsCommandEvent {

    public TicketCompleteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.DONE_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketComplete(this.ticket));
    }

    @Override
    public @NonNull TargetPair target(final @NonNull MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketComplete(this.ticket, this.soul));
    }

    @Override
    public @NonNull Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketComplete(this.ticket, this.soul);
    }

}
