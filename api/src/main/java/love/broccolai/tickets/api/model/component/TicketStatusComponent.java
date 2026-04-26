package love.broccolai.tickets.api.model.component;

import love.broccolai.tickets.api.model.TicketStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketStatusComponent(
    TicketStatus status
) implements TicketComponent {

    public static final ComponentKey<TicketStatusComponent> KEY = ComponentKey.of(
        "tickets:status",
        TicketStatusComponent.class
    );

    @Override
    public ComponentKey<TicketStatusComponent> key() {
        return KEY;
    }
}
