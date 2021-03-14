package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TicketCompleteEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final OnlineSoul soul;
    private final Ticket ticket;

    public TicketCompleteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    @Override
    public OnlineSoul soul() {
        return this.soul;
    }

    @Override
    public Ticket ticket() {
        return this.ticket;
    }

    @Override
    public void sender(@NonNull final MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketComplete(this.ticket));
    }

    @Override
    public @Nullable Component staff(@NonNull final MessageService messageService) {
        return messageService.staffTicketComplete(this.ticket);
    }

}
