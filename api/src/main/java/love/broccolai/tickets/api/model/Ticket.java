package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Ticket {

    int id();

    TicketFormat type();

    UUID creator();

    Instant date();

    Set<Action> actions();

    void withAction(Action action);

    TicketStatus status();

    TicketFormatContent content();

    Optional<UUID> assignee();
}
