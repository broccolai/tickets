package love.broccolai.tickets.common.model;

import java.time.Instant;
import java.util.SortedSet;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.MessageAction;
import love.broccolai.tickets.api.model.action.StatusModificationAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SimpleTicket(
    int id,
    UUID creator,
    Instant date,
    SortedSet<Action> actions
) implements Ticket {

    @Override
    public TicketStatus status() {
        return Trove.of(this.actions)
            .filterIsInstance(StatusModificationAction.class)
            .first()
            .orElseThrow()
            .status();
    }

    @Override
    public String message() {
        return Trove.of(this.actions)
            .filterIsInstance(MessageAction.class)
            .last()
            .orElseThrow()
            .message();
    }
}
