package love.broccolai.tickets.common.model;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.StatusAction;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SimpleTicket(
    int id,
    TicketFormat type,
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

    //todo: implement updating
    @Override
    public TicketFormatContent content() {
        return Trove.of(this.actions)
            .filterIsInstance(OpenAction.class)
            .first()
            .map(OpenAction::content)
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
