package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public record EditAction(
        @NonNull Instant creationDate,
        @NonNull UUID creator,
        @NonNull String message
) implements Action {

}
