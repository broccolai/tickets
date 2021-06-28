package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.notification.NotificationReason;
import broccolai.tickets.api.model.event.notification.TicketsCommandEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class TicketNoteEvent extends TicketsCommandEvent {

    private final String note;

    public TicketNoteEvent(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket, final @NonNull String note) {
        super(NotificationReason.NOTE_TICKET, soul, ticket);
        this.note = note;
    }

    public @NonNull String note() {
        return this.note;
    }

    @Override
    public void sender(final @NonNull OldMessageService oldMessageService) {
        Component component = oldMessageService.senderTicketNote(this.ticket);
        this.soul.sendMessage(component);
    }

    @Override
    public @NotNull TargetPair target(final @NonNull OldMessageService oldMessageService) {
        return new TargetPair(this.ticket.player(), oldMessageService.targetTicketNote(this.ticket, this.note, this.soul));
    }

    @Override
    public @NonNull Component staff(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.staffTicketNote(this.ticket, this.note, this.soul);
    }

}
