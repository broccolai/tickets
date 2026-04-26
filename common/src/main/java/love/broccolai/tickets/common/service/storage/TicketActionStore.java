package love.broccolai.tickets.common.service.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.ActionProperties;
import love.broccolai.tickets.api.model.action.ActionRegistry;
import love.broccolai.tickets.api.model.action.TicketAction;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import org.jdbi.v3.core.Handle;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketActionStore {

    private final QueriesLocator locator;
    private final ActionRegistry actionRegistry;
    private final TicketPropertyStore properties;

    public TicketActionStore(
        final QueriesLocator locator,
        final ActionRegistry actionRegistry,
        final TicketPropertyStore properties
    ) {
        this.locator = locator;
        this.actionRegistry = actionRegistry;
        this.properties = properties;
    }

    public int insert(final Handle handle, final int ticketId, final TicketAction action) {
        int actionId = handle.createUpdate(this.locator.query("insert-action"))
            .bind("ticket", ticketId)
            .bind("action_type", this.actionRegistry.type(action))
            .bindByType("actor", action.creator(), UUID.class)
            .bindByType("occurred_at", action.date(), Instant.class)
            .executeAndReturnGeneratedKeys()
            .mapTo(Integer.class)
            .one();

        this.properties.save(handle, actionId, this.actionRegistry.encode(action));

        return actionId;
    }

    public List<TicketAction> loadAll(final Handle handle, final int ticketId) {
        return handle.createQuery(this.locator.query("select-actions"))
            .bind("ticket", ticketId)
            .map((resultSet, context) -> ActionRow.from(resultSet))
            .map(actionRow -> this.action(handle, actionRow))
            .list();
    }

    public StoredTicketAction load(final Handle handle, final int actionId) {
        ActionRow actionRow = handle.createQuery(this.locator.query("select-action"))
            .bind("id", actionId)
            .map((resultSet, context) -> ActionRow.from(resultSet))
            .findFirst()
            .orElseThrow();

        return new StoredTicketAction(actionRow.actionId(), actionRow.ticketId(), this.action(handle, actionRow));
    }

    private TicketAction action(final Handle handle, final ActionRow actionRow) {
        return this.actionRegistry.decode(
            actionRow.actionType(),
            actionRow.occurredAt(),
            actionRow.actor(),
            new ActionProperties(this.properties.loadAction(handle, actionRow.actionId()).decoded())
        );
    }

    private record ActionRow(
        int actionId,
        int ticketId,
        String actionType,
        UUID actor,
        Instant occurredAt
    ) {

        static ActionRow from(final ResultSet resultSet) throws SQLException {
            Timestamp occurredAt = resultSet.getTimestamp("occurred_at");

            return new ActionRow(
                resultSet.getInt("id"),
                resultSet.getInt("ticket"),
                resultSet.getString("action_type"),
                resultSet.getObject("actor", UUID.class),
                occurredAt.toInstant()
            );
        }
    }
}
