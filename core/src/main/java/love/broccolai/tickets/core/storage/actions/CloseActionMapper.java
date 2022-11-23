package love.broccolai.tickets.core.storage.actions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.action.CloseAction;
import love.broccolai.tickets.core.storage.ActionMapper;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class CloseActionMapper implements ActionMapper<CloseAction> {

    @Override
    public CloseAction map(
            final ColumnMapper<UUID> mapper,
            final Instant date,
            final ResultSet rs,
            final StatementContext ctx
    ) throws SQLException {
        UUID creator = mapper.map(rs, "action_creator", ctx);
        String message = rs.getString("action_message");

        return new CloseAction(date, creator, message);
    }

    @Override
    public Map<Entries, Object> processBindables(final CloseAction action) {
        Map<Entries, Object> result = new HashMap<>();

        result.put(Entries.DATE, action.date());
        result.put(Entries.CREATOR, action.creator());

        if (action.message() != null) {
            result.put(Entries.MESSAGE, action.message());
        }

        return result;
    }

}
