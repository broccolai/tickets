package broccolai.tickets.api.model.ticket;

import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Ticket {

    private final int id;
    private final UUID uuid;
    private final Status status;
    private final UUID claimer;

    public Ticket(final int id, final @NonNull UUID uuid, final @NonNull Status status, final @Nullable UUID claimer) {
        this.id = id;
        this.uuid = uuid;
        this.status = status;
        this.claimer = claimer;
    }

    public int id() {
        return this.id;
    }

    public @NonNull UUID uuid() {
        return this.uuid;
    }

    public @NonNull Status status() {
        return this.status;
    }

    public @NonNull Optional<UUID> claimer() {
        return Optional.ofNullable(this.claimer);
    }

    public Ticket withStatus(@NonNull Status status) {
        return new Ticket(this.id, this.uuid, status, this.claimer);
    }

    public Ticket withClaimer(@Nullable UUID claimer) {
        return new Ticket(this.id, this.uuid, this.status, claimer);
    }

    public enum Status {
        OPEN,
        CLAIMED,
        CLOSED;
    }

}
