package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record AssignAction(
    Instant date,
    UUID creator,
    UUID assignee
) implements Action {

    public static final String IDENTIFIER = "assign";

}
