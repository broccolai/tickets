package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public final class TicketAssignEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final OnlineSoul soul;
    private final Soul target;
    private final Ticket ticket;

    public TicketAssignEvent(final @NonNull OnlineSoul soul, final @NonNull Soul target, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.target = target;
        this.ticket = ticket;
    }

    @Override
    public OnlineSoul soul() {
        return this.soul;
    }

    public Soul target() {
        return this.target;
    }

    @Override
    public Ticket ticket() {
        return this.ticket;
    }

    @Override
    public void sender(@NonNull final MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketAssign(this.ticket, this.target));
    }

    @Override
    public @NotNull TargetPair target(@NonNull final MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketClaim(this.ticket));
    }

    @Override
    public @Nullable Component staff(@NonNull final MessageService messageService) {
        return messageService.staffTicketAssign(this.ticket);
    }

}
