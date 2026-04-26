package love.broccolai.tickets.api.model.component;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketAssignment(
    UUID assignee
) implements TicketComponent {

    public static final ComponentKey<TicketAssignment> KEY = ComponentKey.of(
        "tickets:assignment",
        TicketAssignment.class
    );

    @Override
    public ComponentKey<TicketAssignment> key() {
        return KEY;
    }
}
