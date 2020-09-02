package broccolai.tickets.storage.functions;

import broccolai.tickets.interactions.PendingNotification;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.storage.platforms.Platform;
import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.MessageReason;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import co.aikar.idb.DbRow;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for general sql functions.
 */
public class HelpersSQL {
    private Platform platform;
    private MessageSQL messageSQL;

    /**
     * Setup the helper SQL.
     *
     * @param platform   the platform instance
     * @param messageSQL the message SQL
     */
    public void setup(@NotNull Platform platform, @NotNull MessageSQL messageSQL) {
        this.platform = platform;
        this.messageSQL = messageSQL;
    }

    /**
     * Retrieve a UUID from a column.
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed uuid
     */
    @Nullable
    UUID getUUID(@NotNull DbRow row, @NotNull String column) {
        String raw = row.getString(column);

        if (raw == null || raw.equals("null")) {
            return null;
        } else {
            return UUID.fromString(raw);
        }
    }

    /**
     * Retrieve a Location from a column.
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed location
     */
    @NotNull
    Location getLocation(@NotNull DbRow row, @NotNull String column) {
        String raw = row.getString(column);
        String[] split = raw.split("\\|");
        String rawWorld = split[0];
        World world = !rawWorld.equals("null") ? Bukkit.getWorld(rawWorld) : null;

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    /**
     * Serialise a location into a string.
     *
     * @param location a location instance
     * @return a string representation
     */
    @NotNull
    String serializeLocation(@NotNull Location location) {
        World world = location.getWorld();
        return (world != null ? world.getName() : "null") + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
    }

    /**
     * Retrieve a date from a column.
     *
     * @param row    the database row
     * @param column the column to look in
     * @return the constructed LocalDateTime
     */
    @NotNull
    LocalDateTime getDate(@NotNull DbRow row, @NotNull String column) {
        Instant instant = Instant.ofEpochSecond(platform.getPureLong(row, column));

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Serialise a LocalDateTime to a long.
     *
     * @param time the LocalDateTime instance
     * @return a long representation
     */
    @NotNull
    Long serializeLocalDateTime(@NotNull LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * Retrieve an enum value from a row.
     *
     * @param row    the database row
     * @param clazz  the enum class
     * @param column the column to lookup
     * @param <T>    the class type
     * @return an enum
     */
    @NotNull <T extends Enum<T>> T getEnumValue(@NotNull DbRow row, @NotNull Class<T> clazz, @NotNull String column) {
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
     * Build a ticket from a row.
     *
     * @param row the database row to use
     * @return the constructed ticket
     */
    Ticket buildTicket(DbRow row) {
        Integer id = row.getInt("id");
        UUID player = getUUID(row, "uuid");
        List<Message> messages = messageSQL.selectAll(row.getInt("id"));
        TicketStatus status = getEnumValue(row, TicketStatus.class, "status");
        Location location = getLocation(row, "location");
        UUID picker = getUUID(row, "picker");

        assert player != null;
        return new Ticket(id, player, messages, location, status, picker);
    }

    /**
     * Build a ticket from a row.
     *
     * @param row the database row to use
     * @return the constructed message
     */
    Message buildMessage(DbRow row) {
        MessageReason reason = getEnumValue(row, MessageReason.class, "reason");
        LocalDateTime date = getDate(row, "date");

        String data = row.getString("data");
        UUID sender = getUUID(row, "sender");

        return new Message(reason, date, data, sender);
    }

    /**
     * Build a notification from a row.
     *
     * @param row the database row to use
     * @return the constructed notification
     */
    PendingNotification buildNotification(DbRow row) {
        Messages message = getEnumValue(row, Messages.class, "message");
        String[] replacements = row.getString("replacements").split("\\|");

        return new PendingNotification(message, replacements);
    }
}
