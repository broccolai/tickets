package love.broccolai.tickets.core.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

public final class TicketAccumulator implements RowReducer<Map<Integer, Ticket.Builder>, Ticket> {

    @Override
    public Map<Integer, Ticket.Builder> container() {
        return new HashMap<>();
    }

    @Override
    public void accumulate(final Map<Integer, Ticket.Builder> container, final RowView row) {
        Ticket.Builder ticket = container.computeIfAbsent(
                row.getColumn("id", Integer.class),
                id -> row.getRow(Ticket.Builder.class)
        );

        if (row.getColumn("type", String.class) != null) {
            Action action = row.getRow(Action.class);
            ticket.withAction(action);
        }
    }

    @Override
    public Stream<Ticket> stream(final Map<Integer, Ticket.Builder> container) {
        return container.values()
                .stream()
                .map(Ticket.Builder::build);
    }

}
