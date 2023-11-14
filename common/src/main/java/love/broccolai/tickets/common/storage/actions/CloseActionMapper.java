package love.broccolai.tickets.common.storage.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.packaged.CloseAction;
import love.broccolai.tickets.common.storage.ActionMapper;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CloseActionMapper extends ActionMapper<CloseAction> {

    public static final CloseActionMapper INSTANCE = new CloseActionMapper();

    @Override
    public CloseAction map(
        final ColumnMapper<UUID> mapper,
        final UUID creator,
        final Instant date,
        final ResultSet rs,
        final StatementContext ctx
    ) throws SQLException {
        return new CloseAction(date, creator);
    }

    @Override
    protected Map<Entries, Object> processBindables(final CloseAction action) {
        return Map.of();
    }

}
