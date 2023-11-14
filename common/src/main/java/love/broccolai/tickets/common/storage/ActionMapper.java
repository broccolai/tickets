package love.broccolai.tickets.common.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Locale.ROOT;

@NullMarked
public abstract class ActionMapper<A extends Action> implements RowMapper<Action> {

    @Override
    public final Action map(ResultSet rs, StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);
        UUID creator = uuidMapper.map(rs, "action_creator", ctx);
        Instant date = rs.getTimestamp("action_date").toInstant();

        return this.map(uuidMapper, creator, date, rs, ctx);
    }

    protected abstract A map(
        ColumnMapper<UUID> mapper,
        UUID creator,
        Instant date,
        ResultSet rs,
        StatementContext ctx
    ) throws SQLException;

    public Map<String, @Nullable Object> bindables(A action) {
        Map<String, @Nullable Object> binds = new HashMap<>();

        for (Entries value : Entries.values()) {
            binds.put(value.name().toLowerCase(ROOT), null);
        }

        this.processBindables(action).forEach((entry, value) -> {
            binds.put(entry.name().toLowerCase(ROOT), value);
        });

        return binds;
    }

    protected abstract Map<Entries, Object> processBindables(A action);

    protected enum Entries {
        ASSIGNEE,
        MESSAGE
    }
}
