package broccolai.tickets.core.storage.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class ComponentMapper implements ColumnMapper<Component> {

    public static final MiniMessage MINI = MiniMessage.get();

    @Override
    public Component map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        return MINI.deserialize(r.getString(columnNumber));
    }

    @Override
    public Component map(final ResultSet r, final String columnLabel, final StatementContext ctx) throws SQLException {
        return MINI.deserialize(r.getString(columnLabel));
    }

}
