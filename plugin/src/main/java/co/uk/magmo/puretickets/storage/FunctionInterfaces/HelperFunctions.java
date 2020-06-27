package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.aikar.idb.DbRow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public interface HelperFunctions {
    default @Nullable UUID getUUID(DbRow row, String column) {
        String raw = row.getString(column);

        if (raw == null || raw.equals("null")) {
            return null;
        } else {
            return UUID.fromString(raw);
        }
    }

    default Location getLocation(DbRow row, String column) {
        String raw = row.getString(column);
        String[] split = raw.split("\\|");
        World world = Bukkit.getWorld(split[0]);

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    default String serializeLocation(Location location) {
        return location.getWorld() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
    }

    default Long getPureLong(DbRow row, String column) {
        return row.getLong(column);
    }

    default LocalDateTime getDate(DbRow row, String column) {
        Instant instant = Instant.ofEpochSecond(getPureLong(row, column));

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Long serializeLocalDateTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    default <T extends Enum<T>> T getEnumValue(DbRow row, Class<T> clazz, String column) {
        T[] enumConstants = clazz.getEnumConstants();
        String raw = row.getString(column);

        for (T enumConstant : enumConstants) {
            if (raw.equals(enumConstant.name())) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException();
    }
}
