package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.component.TicketComponent;
import love.broccolai.tickets.api.model.component.TicketComponents;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketComponentAttached(
    Instant date,
    UUID creator,
    TicketComponent component
) implements TicketAction {

    public static final String TYPE = "tickets:component_attached";

    @Override
    public TicketComponents apply(final TicketComponents components) {
        return components.with(this.component);
    }
}
