package love.broccolai.tickets.core.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.Action;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public interface ActionMapper<A extends Action> extends RowMapper<Action> {

    @Override
    default Action map(ResultSet rs, StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);
        Instant date = rs.getTimestamp("action_date").toInstant();

        return this.map(uuidMapper, date, rs, ctx);
    }

    A map(ColumnMapper<UUID> mapper, Instant date, ResultSet rs, StatementContext ctx) throws SQLException;

    Map<Entries, Object> processBindables(A action);

    enum Entries {
        DATE,
        CREATOR,
        ASSIGNEE,
        MESSAGE
    }
}
