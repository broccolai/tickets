package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.MessageAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record CommentAction(
    Instant date,
    UUID creator,
    String message
) implements MessageAction {

    public static final String IDENTIFIER = "comment";

}
