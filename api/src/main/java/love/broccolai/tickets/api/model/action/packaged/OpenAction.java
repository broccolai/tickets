package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.MessageAction;
import love.broccolai.tickets.api.model.action.StatusModificationAction;

public record OpenAction(
    Instant date,
    UUID creator,
    String message
) implements StatusModificationAction, MessageAction {

    @Override
    public TicketStatus status() {
        return TicketStatus.OPEN;
    }
}
