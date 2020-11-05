package broccolai.tickets.storage.mapper;

import broccolai.tickets.message.Message;
import broccolai.tickets.ticket.Ticket;
import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class TicketReducer implements RowReducer<Map<Integer, Ticket>, Ticket> {

    @Override
    public Map<Integer, Ticket> container() {
        return new HashMap<>();
    }

    @Override
    public void accumulate(final Map<Integer, Ticket> container, final RowView rowView) {
        int id = rowView.getColumn("id", Integer.class);

        Ticket ticket = container.computeIfAbsent(id, num -> rowView.getRow(Ticket.class));

        if (rowView.getColumn("ticket", Integer.class) != null) {
            ticket.withMessage(rowView.getRow(Message.class));
        }
    }

    @Override
    public Stream<Ticket> stream(final Map<Integer, Ticket> container) {
        return container.values().stream();
    }

}
