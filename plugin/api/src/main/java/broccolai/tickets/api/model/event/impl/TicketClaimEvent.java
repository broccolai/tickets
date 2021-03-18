package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketClaimEvent extends TicketsCommandEvent {

    public TicketClaimEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        super(soul, ticket);
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketClaim(this.ticket));
    }

    @Override
    public TargetPair target(final @NonNull MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketClaim(this.ticket));
    }

    @Override
    public Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketClaim(this.ticket);
    }

    @Override
    public void discord(final @NonNull DiscordService discordService) {

    }

}
