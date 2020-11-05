package broccolai.tickets.storage.mapper;

import broccolai.tickets.user.UserSettings;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SettingsMapper implements RowMapper<UserSettings> {

    @Override
    public UserSettings map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        boolean announcements = rs.getBoolean("announcements");
        return new UserSettings(announcements);
    }

}
