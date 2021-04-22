package broccolai.tickets.core.storage.mapper;

import broccolai.tickets.api.model.position.Position;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public final class PositionMapper implements ColumnMapper<Position> {

    @Override
    public Position map(final ResultSet r, final int columnNumber, final StatementContext ctx) {
        throw new IllegalArgumentException();
    }

    @Override
    public Position map(final ResultSet rs, final String columnLabel, final StatementContext ctx) throws SQLException {
        String raw = rs.getString(columnLabel);
        String[] split = raw.split("\\|");
        String rawWorld = split[0];
        String world = !rawWorld.equals("null") ? rawWorld : null;

        return new Position(
                world,
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3])
        );
    }

    //todo
    public static @NonNull String valueOf(final @NonNull Position position) {
        return position.world() + "|" + position.x() + "|" + position.y() + "|" + position.z();
    }

}
