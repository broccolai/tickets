package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.TicketCommandEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TicketCloseEvent extends TicketCommandEvent {

    public TicketCloseEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        super(soul, ticket);
    }

    @Override
    public void sender(@NonNull final MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketClose(this.ticket));
    }

    @Override
    public @Nullable Component staff(@NonNull final MessageService messageService) {
        return messageService.staffTicketClose(this.ticket);
    }

    @Override
    public void discord(@NonNull final DiscordService discordService) {

    }

}
