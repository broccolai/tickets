package broccolai.tickets.storage.mapper;

import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import org.bukkit.Location;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class TicketMapper implements RowMapper<Ticket> {

    private ColumnMapper<UUID> uuidMapper;
    private ColumnMapper<Location> locationMapper;
    private ColumnMapper<TicketStatus> statusMapper;

    @Override
    public Ticket map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        this.registerMappers(ctx);

        int id = rs.getInt("id");
        UUID creator = uuidMapper.map(rs, "uuid", ctx);
        Location location = locationMapper.map(rs, "location", ctx);
        TicketStatus status = statusMapper.map(rs, "status", ctx);
        UUID assignee = uuidMapper.map(rs, "picker", ctx);

        return new Ticket(id, creator, location, status, assignee);
    }

    private void registerMappers(final StatementContext ctx) {
        if (uuidMapper == null) {
            uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);
        }

        if (locationMapper == null) {
            locationMapper = ctx.findColumnMapperFor(Location.class).orElseThrow(IllegalStateException::new);
        }

        if (statusMapper == null) {
            statusMapper = EnumMapper.byName(TicketStatus.class);
        }
    }

}

