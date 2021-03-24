package broccolai.tickets.api.model.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class Ticket {

    private final int id;
    private final UUID player;
    private final Position position;
    private TicketStatus status;
    private MessageInteraction message;
    private @Nullable UUID claimer;

    public Ticket(
            final int id,
            final @NonNull UUID player,
            final @NonNull Position position,
            final @NonNull TicketStatus status,
            final @NonNull MessageInteraction message,
            final @Nullable UUID claimer
    ) {
        this.id = id;
        this.player = player;
        this.position = position;
        this.status = status;
        this.message = message;
        this.claimer = claimer;
    }

    public int id() {
        return this.id;
    }

    public @NonNull UUID player() {
        return this.player;
    }

    public @NonNull Position position() {
        return this.position;
    }

    public @NonNull TicketStatus status() {
        return this.status;
    }

    public void status(final @NonNull TicketStatus status) {
        this.status = status;
    }

    public @NonNull MessageInteraction message() {
        return this.message;
    }

    public void message(final @NonNull MessageInteraction message) {
        this.message = message;
    }

    public @NonNull Optional<@NonNull UUID> claimer() {
        return Optional.ofNullable(this.claimer);
    }

    public void claimer(final @Nullable UUID claimer) {
        this.claimer = claimer;
    }

}
