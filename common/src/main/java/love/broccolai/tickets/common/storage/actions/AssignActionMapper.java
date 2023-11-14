package love.broccolai.tickets.common.storage.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.packaged.AssignAction;
import love.broccolai.tickets.common.storage.ActionMapper;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AssignActionMapper extends ActionMapper<AssignAction> {

    public static final AssignActionMapper INSTANCE = new AssignActionMapper();

    @Override
    public AssignAction map(
        final ColumnMapper<UUID> mapper,
        final UUID creator,
        final Instant date,
        final ResultSet rs,
        final StatementContext ctx
    ) throws SQLException {
        UUID assignee = mapper.map(rs, "action_assignee", ctx);

        return new AssignAction(date, creator, assignee);
    }

    @Override
    protected Map<Entries, Object> processBindables(final AssignAction action) {
        return Map.of(
            Entries.ASSIGNEE, action.assignee()
        );
    }

}
