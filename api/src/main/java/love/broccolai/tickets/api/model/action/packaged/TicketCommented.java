package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.MessageTicketAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketCommented(
    Instant date,
    UUID creator,
    String message
) implements MessageTicketAction {

    public static final String TYPE = "tickets:commented";
}
