package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketCreateEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final PlayerSoul soul;
    private final Ticket ticket;

    /**
     * Initialise the creation event
     */
    public TicketCreateEvent(final @NonNull PlayerSoul soul, final @NonNull Ticket ticket) {
        this.soul = soul;
        this.ticket = ticket;
    }

    /**
     * Get the constructors soul
     */
    @Override
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

    /**
     * Get the created ticket
     */
    @Override
    public @NonNull Ticket ticket() {
        return this.ticket;
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        Component component = messageService.senderTicketCreation(this.ticket);
        this.soul.sendMessage(component);
    }

    @Override
    public Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketCreate(this.ticket);
    }

    @Override
    public void discord(final @NonNull DiscordService discordService) {

    }

}
