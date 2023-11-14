package love.broccolai.tickets.common.storage;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import love.broccolai.tickets.api.model.action.Action;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.api.model.action.packaged.OpenAction;
import love.broccolai.tickets.common.storage.actions.AssignActionMapper;
import love.broccolai.tickets.common.storage.actions.CloseActionMapper;
import love.broccolai.tickets.common.storage.actions.CommentActionMapper;
import love.broccolai.tickets.common.storage.actions.OpenActionMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DelegatingActionMapper implements RowMapper<Action> {

    private static final BiMap<Class<? extends Action>, String> NAME_MAPPINGS = HashBiMap.create(Map.of(
        OpenAction.class, "OPEN",
        AssignAction.class, "ASSIGN",
        CloseAction.class, "CLOSE",
        CommentAction.class, "EDIT"
    ));

    private static final Map<Class<? extends Action>, ? extends ActionMapper<?>> MAPPERS = Map.of(
        OpenAction.class, OpenActionMapper.INSTANCE,
        AssignAction.class, AssignActionMapper.INSTANCE,
        CloseAction.class, CloseActionMapper.INSTANCE,
        CommentAction.class, CommentActionMapper.INSTANCE
    );

    @SuppressWarnings("unchecked")
    public <T extends Action> Map<String, ?> bindables(final T action) {
        Class<? extends Action> clazz = action.getClass();
        ActionMapper<T> mapper = (ActionMapper<T>) MAPPERS.get(clazz);

        return mapper.bindables(action);
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
