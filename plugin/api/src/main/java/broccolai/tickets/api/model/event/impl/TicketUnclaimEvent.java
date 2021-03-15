package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketUnclaimEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final OnlineSoul soul;
    private final Ticket ticket;

    public TicketUnclaimEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
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
    public void sender(final @NonNull MessageService messageService) {
        this.soul.sendMessage(messageService.senderTicketUnclaim(this.ticket));
    }

    @Override
    public TargetPair target(final @NonNull MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketUnclaim(this.ticket));
    }

    @Override
    public Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketUnclaim(this.ticket);
    }

    @Override
    public void discord(final @NonNull DiscordService discordService) {

    }

}
