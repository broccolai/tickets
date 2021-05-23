package broccolai.tickets.api.model.ticket;

import broccolai.corn.context.Context;
import broccolai.corn.context.MappedContext;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Ticket {

    private final int id;
    private final UUID player;
    private TicketStatus status;
    private MessageInteraction message;
    private @Nullable UUID claimer;

    private final Context context = new MappedContext();

    public Ticket(
            final int id,
            final @NonNull UUID player,
            final @NonNull TicketStatus status,
            final @NonNull MessageInteraction message,
            final @Nullable UUID claimer
    ) {
        this.id = id;
        this.player = player;
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

    public @NonNull Context context() {
        return this.context;
    }

}
