package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Ticket {

    int id();

    TicketType type();

    UUID creator();

    Instant date();

    Set<Action> actions();

    void withAction(Action action);

    TicketStatus status();

    String message();

    Optional<UUID> assignee();
}
