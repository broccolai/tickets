package love.broccolai.tickets.api.model.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TicketComponent {

    ComponentKey<? extends TicketComponent> key();
}
