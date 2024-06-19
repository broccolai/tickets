package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.StatusAction;
import love.broccolai.tickets.api.model.format.TicketFormatContent;

public record OpenAction(
    Instant date,
    UUID creator,
    TicketFormatContent content
) implements StatusAction {

    public static final String IDENTIFIER = "open";

    @Override
    public TicketStatus status() {
        return TicketStatus.OPEN;
    }
}
