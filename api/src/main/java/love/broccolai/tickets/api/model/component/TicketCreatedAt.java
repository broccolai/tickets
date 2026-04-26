package love.broccolai.tickets.api.model.component;

import java.time.Instant;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketCreatedAt(
    Instant date
) implements TicketComponent {

    public static final ComponentKey<TicketCreatedAt> KEY = ComponentKey.of(
        "tickets:created_at",
        TicketCreatedAt.class
    );

    @Override
    public ComponentKey<TicketCreatedAt> key() {
        return KEY;
    }
}
