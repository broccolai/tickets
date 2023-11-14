package love.broccolai.tickets.api.model;

import java.time.Instant;
import java.util.SortedSet;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;

public interface Ticket {

    int id();

    UUID creator();

    Instant date();

    SortedSet<Action> actions();

    TicketStatus status();

    String message();
}
