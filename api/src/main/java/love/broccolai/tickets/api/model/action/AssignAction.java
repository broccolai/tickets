package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record AssignAction(
        Instant date,
        UUID creator,
        UUID assignee
) implements Action {

}
