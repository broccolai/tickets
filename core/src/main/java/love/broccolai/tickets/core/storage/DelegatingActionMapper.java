package love.broccolai.tickets.core.storage;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.AssignAction;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.api.model.action.EditAction;
import love.broccolai.tickets.core.storage.ActionMapper.Entries;
import love.broccolai.tickets.core.storage.actions.AssignActionMapper;
import love.broccolai.tickets.core.storage.actions.CloseActionMapper;
import love.broccolai.tickets.core.storage.actions.EditActionMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DelegatingActionMapper implements RowMapper<Action> {

    private static final BiMap<Class<? extends Action>, String> NAME_MAPPINGS = HashBiMap.create(Map.of(
            AssignAction.class, "ASSIGN",
            CloseAction.class, "CLOSE",
            EditAction.class, "EDIT"
    ));

    private static final Map<Class<? extends Action>, ? extends ActionMapper<?>> MAPPERS = Map.of(
            AssignAction.class, new AssignActionMapper(),
            CloseAction.class, new CloseActionMapper(),
            EditAction.class, new EditActionMapper()
    );

    @SuppressWarnings("unchecked")
    public <T extends Action> Map<String, ?> bindables(final T action) {
        Class<? extends Action> clazz = action.getClass();
        ActionMapper<T> mapper = (ActionMapper<T>) MAPPERS.get(clazz);

        Map<Entries, Object> processedBindings = mapper.processBindables(action);
        Map<String, Object> result = new HashMap<>();

        processedBindings.forEach((key, value) -> {
            result.put(key.name().toLowerCase(Locale.ROOT), value);
        });

        Collection<Entries> entries = new ArrayList<>(Arrays.asList(Entries.values()));
        entries.removeAll(processedBindings.keySet());

        for (final Entries entry : entries) {
            result.put(entry.name().toLowerCase(Locale.ROOT), null);
        }

        return result;
    }

    public String typeIdentifier(final Action action) {
        return NAME_MAPPINGS.get(action.getClass());
    }

    @Override
    public Action map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        String type = rs.getString("type");
        Class<? extends Action> clazz = NAME_MAPPINGS.inverse().get(type);

        return MAPPERS.get(clazz).map(rs, ctx);
    }

}
