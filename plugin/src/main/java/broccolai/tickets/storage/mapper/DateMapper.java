package broccolai.tickets.storage.mapper;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateMapper implements ColumnMapper<LocalDateTime> {

    @Override
    public LocalDateTime map(final ResultSet rs, final int columnNumber, final StatementContext ctx) throws SQLException {
        throw new IllegalArgumentException();
    }

    @Override
    public LocalDateTime map(final ResultSet rs, final String columnLabel, final StatementContext ctx) throws SQLException {
        Instant instant = Instant.ofEpochSecond(rs.getLong(columnLabel));

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
