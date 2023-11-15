package love.broccolai.tickets.common.serialization.jdbi;

import java.util.Map;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketAccumulator implements LinkedHashMapRowReducer<Integer, Ticket> {

    @Override
    public void accumulate(final Map<Integer, Ticket> container, final RowView row) {
        Ticket ticket = container.computeIfAbsent(
            row.getColumn("id", Integer.class),
            id -> row.getRow(Ticket.class)
        );

        ticket.withAction(
            row.getRow(Action.class)
        );
    }
}
