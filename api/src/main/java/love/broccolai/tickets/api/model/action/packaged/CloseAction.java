package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.StatusAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record CloseAction(
    Instant date,
    UUID creator
) implements StatusAction {

    public static final String IDENTIFIER = "close";

    @Override
    public TicketStatus status() {
        return TicketStatus.CLOSED;
    }
}
