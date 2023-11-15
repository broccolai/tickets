package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.MessageAction;
import love.broccolai.tickets.api.model.action.StatusAction;

public record OpenAction(
    Instant date,
    UUID creator,
    String message
) implements StatusAction, MessageAction {

    public static final String IDENTIFIER = "open";

    @Override
    public TicketStatus status() {
        return TicketStatus.OPEN;
    }
}
