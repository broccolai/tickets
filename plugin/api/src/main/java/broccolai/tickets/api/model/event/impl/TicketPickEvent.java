package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketPickEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final OnlineSoul soul;
    private final Ticket ticket;

    public TicketPickEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
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
        this.soul.sendMessage(messageService.senderTicketPick(this.ticket));
    }

    @Override
    public void target(@NonNull final MessageService messageService) {
        messageService.targetTicketPick(this.ticket);
    }

    @Override
    public void staff(@NonNull final MessageService messageService) {
        messageService.staffTicketPick(this.ticket);
    }

    @Override
    public void discord(@NonNull final DiscordService discordService) {

    }

}
