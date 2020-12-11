package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.core.ticket.TicketStats;
import broccolai.tickets.core.ticket.TicketStatus;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class StatsMapper implements RowMapper<TicketStats> {

    @Override
    public TicketStats map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        TicketStats results = new TicketStats();

        results.put(TicketStatus.OPEN, rs.getInt("open"));
        results.put(TicketStatus.PICKED, rs.getInt("picked"));
        results.put(TicketStatus.CLOSED, rs.getInt("closed"));

        return results;
    }

}
