package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.checkerframework.checker.nullness.qual.NonNull;

public record Ticket(
        int id,
        @NonNull UUID creator,
        @NonNull Instant creationDate,
        @NonNull String message,
        @NonNull Set<Action> actions
) {

    public @NonNull Ticket withMessage(final @NonNull String message) {
        return new Ticket(this.id, this.creator, this.creationDate, message, this.actions);
    }

}
