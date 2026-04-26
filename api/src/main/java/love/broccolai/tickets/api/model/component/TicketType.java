package love.broccolai.tickets.api.model.component;

import love.broccolai.tickets.api.model.format.TicketFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketType(
    TicketFormat format
) implements TicketComponent {

    public static final ComponentKey<TicketType> KEY = ComponentKey.of(
        "tickets:type",
        TicketType.class
    );

    @Override
    public ComponentKey<TicketType> key() {
        return KEY;
    }
}
