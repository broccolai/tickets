package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public record CloseAction(
        @NonNull Instant date,
        @NonNull UUID creator,
        @Nullable String message
) implements Action {
}
