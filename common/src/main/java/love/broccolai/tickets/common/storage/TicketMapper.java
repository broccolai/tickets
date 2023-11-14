package love.broccolai.tickets.common.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.common.model.TicketBuilder;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketMapper implements RowMapper<TicketBuilder> {

    @Override
    public TicketBuilder map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);

        int id = rs.getInt("id");
        UUID creator = uuidMapper.map(rs, "creator", ctx);
        Instant date = rs.getTimestamp("date").toInstant();

        return new TicketBuilder(id, creator, date);
    }

}
