package broccolai.tickets.api.model.ticket;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.storage.Dirtyable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.Optional;
import java.util.UUID;

public final class Ticket extends Dirtyable.Impl {

    private final int id;
    private final UUID player;
    private final Position position;

    private @NonNull TicketStatus status;
    private @Nullable UUID picker;

    /**
     * Construct a new Ticket using all values
     *
     * @param id          Tickets id
     * @param player  Players unique id
     * @param status      Tickets current status
     * @param position    Tickets creation position
     * @param picker  Potentially unset pickers unique id
     */
    public Ticket(
            final int id,
            final @NonNull UUID player,
            final @NonNull TicketStatus status,
            final @NonNull Position position,
            final @Nullable UUID picker
    ) {
        this.id = id;
        this.player = player;
        this.status = status;
        this.position = position;
        this.picker = picker;
    }

    /**
     * Retrieve the tickets id
     */
    public int id() {
        return this.id;
    }

    /**
     * Retrieve uuid of creator
     */
    public @NonNull UUID player() {
        return this.player;
    }

    /**
     * Retrieve the tickets current status
     */
    public @NonNull TicketStatus status() {
        return this.status;
    }

    public void status(final @NonNull TicketStatus status) {
        this.status = status;
    }

    /**
     * Retrieve the tickets creation position
     */
    public @NonNull Position position() {
        return this.position;
    }

    /**
     * Retrieve the pickers uuid
     */
    public @NonNull Optional<UUID> picker() {
        return Optional.ofNullable(this.picker);
    }

    public void picker(final @Nullable UUID picker) {
        this.picker = picker;
    }

}
