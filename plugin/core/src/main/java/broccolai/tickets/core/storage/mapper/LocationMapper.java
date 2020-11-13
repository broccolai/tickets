package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.core.utilities.TicketLocation;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class LocationMapper implements ColumnMapper<TicketLocation> {

    @Override
    public TicketLocation map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        throw new IllegalArgumentException();
    }

    @Override
    public TicketLocation map(final ResultSet rs, final String columnLabel, final StatementContext ctx) throws SQLException {
        String raw = rs.getString(columnLabel);
        String[] split = raw.split("\\|");
        String rawWorld = split[0];
        String world = !rawWorld.equals("null") ? rawWorld : null;

        return new TicketLocation(
                world,
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])
        );
    }

}
