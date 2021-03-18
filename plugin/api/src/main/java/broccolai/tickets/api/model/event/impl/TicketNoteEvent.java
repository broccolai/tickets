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
import org.jetbrains.annotations.NotNull;

public final class TicketNoteEvent implements TicketEvent, SoulEvent, NotificationEvent {

    private final OnlineSoul soul;
    private final Ticket ticket;
    private final String note;

    /**
     * Initialise the note event
     */
    public TicketNoteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket, final @NonNull String note) {
        this.soul = soul;
        this.ticket = ticket;
        this.note = note;
    }

    /**
     * Get the noters soul
     */
    @Override
    public @NonNull OnlineSoul soul() {
        return this.soul;
    }

    /**
     * Get the created ticket
     */
    @Override
    public @NonNull Ticket ticket() {
        return this.ticket;
    }

    public @NonNull String note() {
        return this.note;
    }

    @Override
    public void sender(final @NonNull MessageService messageService) {
        Component component = messageService.senderTicketNote(this.ticket);
        this.soul.sendMessage(component);
    }

    @Override
    public @NotNull TargetPair target(@NonNull final MessageService messageService) {
        return TargetPair.of(this.ticket.player(), messageService.targetTicketNote(this.ticket));
    }

    @Override
    public Component staff(final @NonNull MessageService messageService) {
        return messageService.staffTicketNote(this.ticket, this.note);
    }

    @Override
    public void discord(final @NonNull DiscordService discordService) {

    }

}
