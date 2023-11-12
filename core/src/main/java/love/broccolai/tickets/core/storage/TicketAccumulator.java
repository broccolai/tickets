package love.broccolai.tickets.core.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.core.model.TicketBuilder;
import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TicketAccumulator implements RowReducer<Map<Integer, TicketBuilder>, Ticket> {

    @Override
    public Map<Integer, TicketBuilder> container() {
        return new HashMap<>();
    }

    @Override
    public void accumulate(final Map<Integer, TicketBuilder> container, final RowView row) {
        TicketBuilder ticket = container.computeIfAbsent(
                row.getColumn("id", Integer.class),
                id -> row.getRow(TicketBuilder.class)
        );

        String type = row.<@Nullable String>getColumn("type", String.class);

        if (type == null) {
            return;
        }

        Action action = row.getRow(Action.class);
        ticket.withAction(action);
    }

    @Override
    public Stream<Ticket> stream(final Map<Integer, TicketBuilder> container) {
        return container.values()
                .stream()
                .map(TicketBuilder::build);
    }

}
