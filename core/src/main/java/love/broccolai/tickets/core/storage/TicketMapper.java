package love.broccolai.tickets.core.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class TicketMapper implements RowMapper<Ticket.Builder> {

    @Override
    public Ticket.Builder map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);

        int id = rs.getInt("id");
        UUID creator = uuidMapper.map(rs, "creator", ctx);
        Instant creationDate = rs.getTimestamp("creationDate").toInstant();
        UUID assignee = uuidMapper.map(rs, "assignee", ctx);
        String message = rs.getString("message");

        return new Ticket.Builder(id, creator, creationDate, assignee, message);
    }

}
