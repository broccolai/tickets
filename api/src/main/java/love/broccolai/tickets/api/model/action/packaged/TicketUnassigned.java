package love.broccolai.tickets.api.model.action.packaged;

import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.action.StatusTicketAction;
import love.broccolai.tickets.api.model.component.TicketAssignment;
import love.broccolai.tickets.api.model.component.TicketComponents;
import love.broccolai.tickets.api.model.component.TicketStatusComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketUnassigned(
    Instant date,
    UUID creator
) implements StatusTicketAction {

    public static final String TYPE = "tickets:unassigned";

    @Override
    public TicketStatus status() {
        return TicketStatus.OPEN;
    }

    @Override
    public TicketComponents apply(final TicketComponents components) {
        return components
            .without(TicketAssignment.KEY)
            .with(new TicketStatusComponent(this.status()));
    }
}
