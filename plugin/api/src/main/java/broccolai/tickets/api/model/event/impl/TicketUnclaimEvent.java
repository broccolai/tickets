package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketUnclaimEvent extends TicketsCommandEvent {

    public TicketUnclaimEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(NotificationReason.UNCLAIM_TICKET, soul, ticket);
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        this.soul.sendMessage(oldMessageService.senderTicketUnclaim(this.ticket));
    }

    @Override
    public TargetPair target(final @NonNull OldMessageService oldMessageService) {
        return new TargetPair(this.ticket.player(), oldMessageService.targetTicketUnclaim(this.ticket, this.soul));
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketUnclaim(this.ticket, this.soul);
    }

}
