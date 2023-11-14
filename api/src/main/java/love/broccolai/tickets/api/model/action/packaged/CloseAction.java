package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.StatusModificationAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record CloseAction(
    Instant date,
    UUID creator
) implements StatusModificationAction {

    @Override
    public TicketStatus status() {
        return TicketStatus.CLOSED;
    }
}
