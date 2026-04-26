package love.broccolai.tickets.common.model;

import java.util.ArrayList;
import java.util.List;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.api.model.component.TicketComponents;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SimpleTicket(
    int id,
    TicketComponents components,
    List<TicketAction> actions
) implements Ticket {

    public SimpleTicket {
        actions = List.copyOf(actions);
    }

    @Override
    public Ticket withAction(final TicketAction action) {
        List<TicketAction> updatedActions = new ArrayList<>(this.actions);
        updatedActions.add(action);

        return new SimpleTicket(
            this.id,
            action.apply(this.components),
            updatedActions
        );
    }
}
