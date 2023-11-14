package love.broccolai.tickets.common.storage.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.common.storage.ActionMapper;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CommentActionMapper extends ActionMapper<CommentAction> {

    public static final CommentActionMapper INSTANCE = new CommentActionMapper();

    @Override
    public CommentAction map(
        final ColumnMapper<UUID> mapper,
        final UUID creator,
        final Instant date,
        final ResultSet rs,
        final StatementContext ctx
    ) throws SQLException {
        String message = rs.getString("action_message");

        return new CommentAction(date, creator, message);
    }

    @Override
    protected Map<Entries, Object> processBindables(final CommentAction action) {
        return Map.of(
            Entries.MESSAGE, action.message()
        );
    }

}
