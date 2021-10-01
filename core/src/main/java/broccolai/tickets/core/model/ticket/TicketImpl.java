package broccolai.tickets.core.model.ticket;

import broccolai.corn.context.Context;
import broccolai.corn.context.MappedContext;
import broccolai.tickets.api.model.interaction.Interactions;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.core.model.interaction.TreeSetInteractions;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public final class TicketImpl implements Ticket {

    private final int id;
    private final UUID player;
    private TicketStatus status;
    private @Nullable UUID claimer;

    private final Context context = new MappedContext();
    private final Interactions interactions = new TreeSetInteractions();

    public TicketImpl(
            final int id,
            final @NonNull UUID player,
            final @NonNull TicketStatus status,
            final @Nullable UUID claimer
    ) {
        this.id = id;
        this.player = player;
        this.status = status;
        this.claimer = claimer;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public @NonNull UUID player() {
        return this.player;
    }

    @Override
    public @NonNull TicketStatus status() {
        return this.status;
    }

    @Override
    public void status(final @NonNull TicketStatus status) {
        this.status = status;
    }

    @Override
    public @NonNull Optional<@NonNull UUID> claimer() {
        return Optional.ofNullable(this.claimer);
    }

    @Override
    public void claimer(final @Nullable UUID claimer) {
        this.claimer = claimer;
    }

    @Override
    public @NonNull Context context() {
        return this.context;
    }

    @Override
    public @NonNull Interactions interactions() {
        return this.interactions;
    }

    @Override
    public int compareTo(final @NotNull Ticket target) {
        return Integer.compare(this.id(), target.id());
    }

}
