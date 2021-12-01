package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.Ticket.Status;
import broccolai.tickets.core.model.ticket.TicketImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class TicketMapper implements RowMapper<Ticket> {

    @Override
    public Ticket map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);

        int id = rs.getInt("id");
        UUID player = uuidMapper.map(rs, "uuid", ctx);
        Status status = EnumMapper.byName(Status.class).map(rs, "status", ctx);
        UUID claimer = uuidMapper.map(rs, "claimer", ctx);

        return new TicketImpl(id, player, status, claimer);
    }

}

