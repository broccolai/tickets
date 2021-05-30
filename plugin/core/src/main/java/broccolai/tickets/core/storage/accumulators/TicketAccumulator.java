package broccolai.tickets.core.storage.accumulators;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.core.model.context.ContextKeyValuePair;
import org.jdbi.v3.core.result.RowView;
import java.util.Map;
import java.util.function.BiFunction;

public final class TicketAccumulator implements BiFunction<Map<Integer, Ticket>, RowView, Map<Integer, Ticket>> {

    @Override
    public Map<Integer, Ticket> apply(final Map<Integer, Ticket> container, final RowView row) {
        Ticket ticket = container.computeIfAbsent(
                row.getColumn("id", Integer.class),
                id -> row.getRow(Ticket.class)
        );

        if (row.getColumn("action", String.class) != null) {
            Interaction interaction = row.getRow(Interaction.class);
            ticket.interactions().add(interaction);
        }

        if (row.getColumn("namespace", String.class) != null) {
            //noinspection unchecked
            ContextKeyValuePair<Object> contextPair = row.getRow(ContextKeyValuePair.class);
            ticket.context().put(contextPair.key(), contextPair.value());
        }

        return container;
    }

}
