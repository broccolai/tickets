package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public final class TicketAssignEvent extends TicketsCommandEvent {

    private final Soul target;

    public TicketAssignEvent(final @NonNull OnlineSoul soul, final @NonNull Soul target, final @NonNull Ticket ticket) {
        super(NotificationReason.ASSIGN_TICKET, soul, ticket);
        this.target = target;
    }

    public Soul target() {
        return this.target;
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketAssign(this.ticket, this.target));
    }

    @Override
    public @NotNull TargetPair target(final @NonNull MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketClaim(this.ticket));
    }

    @Override
    public @Nullable Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketAssign(this.ticket);
    }

}
