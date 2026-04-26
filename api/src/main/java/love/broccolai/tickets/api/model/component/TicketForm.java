package love.broccolai.tickets.api.model.component;

import love.broccolai.tickets.api.model.format.TicketFormData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketForm(
    TicketFormData data
) implements TicketComponent {

    public static final ComponentKey<TicketForm> KEY = ComponentKey.of(
        "tickets:form",
        TicketForm.class
    );

    @Override
    public ComponentKey<TicketForm> key() {
        return KEY;
    }
}
