package love.broccolai.tickets.core.storage;

import java.util.Map;
import java.util.function.BiFunction;
import love.broccolai.tickets.api.model.Ticket;
import org.jdbi.v3.core.result.RowView;

public final class TicketAccumulator implements BiFunction<Map<Integer, Ticket>, RowView, Map<Integer, Ticket>> {

    @Override
    public Map<Integer, Ticket> apply(final Map<Integer, Ticket> container, final RowView row) {
        Ticket ticket = row.getRow(Ticket.class);
        container.put(ticket.id(), ticket);

        return container;
    }

}
