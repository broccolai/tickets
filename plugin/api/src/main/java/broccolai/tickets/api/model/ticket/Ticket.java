package broccolai.tickets.api.model.ticket;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.message.Templatable;
import broccolai.tickets.api.model.position.Position;

import java.util.List;

import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class Ticket implements Templatable {

    private final int id;
    private final UUID player;
    private final Position position;
    private TicketStatus status;
    private Interaction message;
    private @Nullable UUID picker;

    public Ticket(
            final int id,
            final @NonNull UUID player,
            final @NonNull Position position,
            final @NonNull TicketStatus status,
            final @NonNull Interaction message,
            final @Nullable UUID picker
    ) {
        this.id = id;
        this.player = player;
        this.position = position;
        this.status = status;
        this.message = message;
        this.picker = picker;
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

    public @NonNull Interaction message() {
        return this.message;
    }

    public void message(final @NonNull Interaction message) {
        this.message = message;
    }

    public @NonNull Optional<UUID> picker() {
        return Optional.ofNullable(this.picker);
    }

    public void picker(final @Nullable UUID picker) {
        this.picker = picker;
    }

    @Override
    public List<Template> templates() {
        //todo
        return null;
    }

}
