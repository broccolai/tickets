package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public record AssignAction(
        @NonNull Instant date,
        @NonNull UUID creator,
        @NonNull UUID assignee
) implements Action {

}