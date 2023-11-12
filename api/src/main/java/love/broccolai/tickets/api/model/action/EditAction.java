package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record EditAction(
        Instant date,
        UUID creator,
        String message
) implements Action {

}
