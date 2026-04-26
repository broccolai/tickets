package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.component.TicketComponents;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TicketAction {

    Instant date();

    UUID creator();

    default TicketComponents apply(final TicketComponents components) {
        return components;
    }
}
