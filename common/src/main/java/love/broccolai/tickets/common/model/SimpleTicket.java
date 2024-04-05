package love.broccolai.tickets.common.model;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.MessageAction;
import love.broccolai.tickets.api.model.action.StatusAction;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SimpleTicket(
    int id,
    TicketType type,
    UUID creator,
    Instant date,
    Set<Action> actions
) implements Ticket {

    @Override
    public void withAction(Action action) {
        this.actions.add(action);
    }

    @Override
    public TicketStatus status() {
        return Trove.of(this.actions)
            .filterIsInstance(StatusAction.class)
            .last()
            .map(StatusAction::status)
            .orElseThrow();
    }

    @Override
    public String message() {
        return Trove.of(this.actions)
            .filterIsInstance(MessageAction.class)
            .last()
            .map(MessageAction::message)
            .orElseThrow();
    }

    @Override
    public Optional<UUID> assignee() {
        return Trove.of(this.actions)
            .filterIsInstance(AssignAction.class)
            .last()
            .map(AssignAction::assignee);
    }
}
