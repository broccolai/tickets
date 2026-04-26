package love.broccolai.tickets.api.model.component;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketCreator(
    UUID creator
) implements TicketComponent {

    public static final ComponentKey<TicketCreator> KEY = ComponentKey.of(
        "tickets:creator",
        TicketCreator.class
    );

    @Override
    public ComponentKey<TicketCreator> key() {
        return KEY;
    }
}
