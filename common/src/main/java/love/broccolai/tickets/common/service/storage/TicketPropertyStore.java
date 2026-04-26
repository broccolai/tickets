package love.broccolai.tickets.common.service.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import love.broccolai.tickets.common.utilities.QueriesLocator;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Update;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketPropertyStore {

    private final QueriesLocator locator;

    public TicketPropertyStore(final QueriesLocator locator) {
        this.locator = locator;
    }

    public void save(
        final Handle handle,
        final int actionId,
        final Map<String, Object> values
    ) {
        this.save(handle, actionId, PropertyBag.of(values));
    }

    public void save(
        final Handle handle,
        final int actionId,
        final PropertyBag properties
    ) {
        properties.values().forEach((path, value) -> this.insert(handle, actionId, path, value));
    }

    public PropertyBag loadAction(final Handle handle, final int actionId) {
        Map<String, PropertyValue> values = new HashMap<>();
        handle.createQuery(this.locator.query("property/select-action-properties"))
            .bind("action", actionId)
            .map((resultSet, context) -> this.property(resultSet))
            .forEach(entry -> values.put(entry.getKey(), entry.getValue()));

        return new PropertyBag(values);
    }

    private Map.Entry<String, PropertyValue> property(final ResultSet resultSet) throws SQLException {
        return Map.entry(resultSet.getString("property"), PropertyValue.from(resultSet));
    }

    private void insert(
        final Handle handle,
        final int actionId,
        final String path,
        final PropertyValue value
    ) {
        Update update = handle.createUpdate(this.locator.query("property/insert-action-property"))
            .bind("action", actionId);

        update.bind("property", path);
        value.bind(update);
        update.execute();
    }
}
