package broccolai.tickets.storage.mapper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class LocationMapper implements ColumnMapper<Location> {

    @Override
    public Location map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        throw new IllegalArgumentException();
    }

    @Override
    public Location map(final ResultSet rs, final String columnLabel, final StatementContext ctx) throws SQLException {
        String raw = rs.getString(columnLabel);
        String[] split = raw.split("\\|");
        String rawWorld = split[0];
        World world = !rawWorld.equals("null") ? Bukkit.getWorld(rawWorld) : null;

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

}
