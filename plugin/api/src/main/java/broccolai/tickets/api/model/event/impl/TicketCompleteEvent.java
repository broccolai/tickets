package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TicketCompleteEvent extends TicketsCommandEvent {

    public TicketCompleteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(soul, ticket);
    }

    @Override
    public void sender(@NonNull final MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketComplete(this.ticket));
    }

    @Override
    public @NonNull TargetPair target(@NonNull final MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketComplete(this.ticket));
    }

    @Override
    public @Nullable Component staff(@NonNull final MessageService messageService) {
        return messageService.staffTicketComplete(this.ticket);
    }

    @Override
    public void discord(@NonNull final DiscordService discordService) {

    }

}
