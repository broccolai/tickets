package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class TicketAssignEvent extends TicketsCommandEvent {

    private final Soul target;

    public TicketAssignEvent(final @NonNull OnlineSoul soul, final @NonNull Soul target, final @NonNull Ticket ticket) {
        super(NotificationReason.ASSIGN_TICKET, soul, ticket);
        this.target = target;
    }

    public @NonNull Soul targetSoul() {
        return this.target;
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        this.soul.sendMessage(oldMessageService.senderTicketAssign(this.ticket, this.target));
    }

    @Override
    public @NotNull TargetPair target(final @NonNull OldMessageService oldMessageService) {
        return new TargetPair(this.ticket.player(), oldMessageService.targetTicketClaim(this.ticket, this.target));
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketAssign(this.ticket, this.target);
    }

}
