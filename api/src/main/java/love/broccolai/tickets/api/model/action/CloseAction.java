package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record CloseAction(
    Instant date,
    UUID creator,
    @Nullable String message
) implements Action {
}
