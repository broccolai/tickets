package broccolai.tickets.storage.functions;

import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.MessageReason;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import co.aikar.idb.DbRow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * Helper class for general sql functions
 */
public final class HelpersSQL {

    private static Platform platform;

    private HelpersSQL() {
    }

    /**
     * Setup the helper SQL
     *
     * @param platformInstance the platform instance
     */
    public static void setup(@NotNull final Platform platformInstance) {
        platform = platformInstance;
    }

    /**
     * Retrieve a UUID from a column
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed uuid
     */
    @Nullable
    static UUID getUUID(@NotNull final DbRow row, @NotNull final String column) {
        String raw = row.getString(column);

        if (raw == null || raw.equals("null")) {
            return null;
        } else {
            return UUID.fromString(raw);
        }
    }

    /**
     * Retrieve a Location from a column
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed location
     */
    @NotNull
    static Location getLocation(@NotNull final DbRow row, @NotNull final String column) {
        String raw = row.getString(column);
        String[] split = raw.split("\\|");
        String rawWorld = split[0];
        World world = !rawWorld.equals("null") ? Bukkit.getWorld(rawWorld) : null;

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    /**
     * Serialise a location into a string
     *
     * @param location a location instance
     * @return a string representation
     */
    @NotNull
    static String serializeLocation(@NotNull final Location location) {
        World world = location.getWorld();
        return (world != null
                ? world.getName()
                : "null") + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
    }

    /**
     * Retrieve a date from a column
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed LocalDateTime
     */
    @NotNull
    static LocalDateTime getDate(@NotNull final DbRow row, @NotNull final String column) {
        Instant instant = Instant.ofEpochSecond(platform.getPureLong(row, column));

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Serialise a LocalDateTime to a long
     *
     * @param time the LocalDateTime instance
     * @return a long representation
     */
    @NotNull
    static Long serializeLocalDateTime(@NotNull final LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * Retrieve an enum value from a row
     *
     * @param row    the database row
     * @param clazz  the enum class
     * @param column the column to lookup
     * @param <T>    the class type
     * @return an enum
     */
    @NotNull
    static <T extends Enum<T>> T getEnumValue(
            @NotNull final DbRow row,
            @NotNull final Class<T> clazz,
            @NotNull final String column
    ) {
        T[] enumConstants = clazz.getEnumConstants();
        String raw = row.getString(column);

        for (T enumConstant : enumConstants) {
            if (raw.equals(enumConstant.name())) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * Build a ticket from a row
     *
     * @param row the database row to use
     * @return the constructed ticket
     */
    static Ticket buildTicket(@NotNull final DbRow row) {
        Integer id = row.getInt("id");
        UUID player = getUUID(row, "uuid");
        List<Message> messages = MessageSQL.selectAll(row.getInt("id"));
        TicketStatus status = getEnumValue(row, TicketStatus.class, "status");
        Location location = getLocation(row, "location");
        UUID picker = getUUID(row, "picker");

        assert player != null;
        return new Ticket(id, player, messages, location, status, picker);
    }

    /**
     * Build a ticket from a row
     *
     * @param row the database row to use
     * @return the constructed message
     */
    static Message buildMessage(@NotNull final DbRow row) {
        MessageReason reason = getEnumValue(row, MessageReason.class, "reason");
        LocalDateTime date = getDate(row, "date");

        String data = row.getString("data");
        UUID sender = getUUID(row, "sender");

        return new Message(reason, date, data, sender);
    }

}
