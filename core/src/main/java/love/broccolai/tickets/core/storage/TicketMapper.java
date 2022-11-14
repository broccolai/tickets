package love.broccolai.tickets.core.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.core.model.TicketBuilder;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class TicketMapper implements RowMapper<TicketBuilder> {

    private final ColumnMapper<TicketStatus> statusMapper = EnumMapper.byName(TicketStatus.class);

    @Override
    public TicketBuilder map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);

        int id = rs.getInt("id");
        TicketStatus status = this.statusMapper.map(rs, "status", ctx);
        UUID creator = uuidMapper.map(rs, "creator", ctx);
        Instant date = rs.getTimestamp("date").toInstant();
        UUID assignee = uuidMapper.map(rs, "assignee", ctx);
        String message = rs.getString("message");

        return new TicketBuilder(id, status, creator, date, assignee, message);
    }

}
