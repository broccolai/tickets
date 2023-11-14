package love.broccolai.tickets.core.storage.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.packaged.CommentAction;
import love.broccolai.tickets.core.storage.ActionMapper;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CommentActionMapper implements ActionMapper<CommentAction> {

    @Override
    public CommentAction map(
        final ColumnMapper<UUID> mapper,
        final Instant date,
        final ResultSet rs,
        final StatementContext ctx
    ) throws SQLException {
        UUID creator = mapper.map(rs, "action_creator", ctx);
        String message = rs.getString("action_message");

        return new CommentAction(date, creator, message);
    }

    @Override
    public Map<Entries, Object> processBindables(final CommentAction action) {
        return Map.of(
            Entries.MESSAGE, action.message()
        );
    }

}
