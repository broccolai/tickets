package broccolai.tickets.storage.mapper;

import broccolai.tickets.ticket.TicketIdStorage;

import broccolai.tickets.ticket.TicketStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.EnumMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class ValueDataMapper implements RowMapper<TicketIdStorage.ValueData> {
    private ColumnMapper<UUID> uuidMapper;
    private ColumnMapper<TicketStatus> statusMapper;

    @Override
    public TicketIdStorage.ValueData map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        this.registerMappers(ctx);

        int id = rs.getInt("id");
        UUID uuid = uuidMapper.map(rs, "uuid", ctx);
        TicketStatus status = statusMapper.map(rs, "status", ctx);

        return new TicketIdStorage.ValueData(id, uuid, status);
    }

    private void registerMappers(final StatementContext ctx) {
        if (uuidMapper == null) {
            uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);
        }

        if (statusMapper == null) {
            statusMapper = EnumMapper.byName(TicketStatus.class);
        }
    }
}
