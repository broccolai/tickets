package co.uk.magmo.puretickets.storage.FunctionInterfaces;

import co.aikar.idb.DbRow;
import co.uk.magmo.puretickets.interactions.PendingNotification;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.ticket.Message;
import co.uk.magmo.puretickets.ticket.MessageReason;
import co.uk.magmo.puretickets.ticket.Ticket;
import co.uk.magmo.puretickets.ticket.TicketStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

public class HelpersSQL {
    @Nullable UUID getUUID(DbRow row, String column) {
        String raw = row.getString(column);

        if (raw == null || raw.equals("null")) {
            return null;
        } else {
            return UUID.fromString(raw);
        }
    }

    Location getLocation(DbRow row, String column) {
        String raw = row.getString(column);
        String[] split = raw.split("\\|");
        World world = Bukkit.getWorld(split[0]);

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    String serializeLocation(Location location) {
        return location.getWorld() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
    }

    Long getPureLong(DbRow row, String column) {
        return row.getLong(column);
    }

    LocalDateTime getDate(DbRow row, String column) {
        Instant instant = Instant.ofEpochSecond(getPureLong(row, column));

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    Long serializeLocalDateTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    <T extends Enum<T>> T getEnumValue(DbRow row, Class<T> clazz, String column) {
        T[] enumConstants = clazz.getEnumConstants();
        String raw = row.getString(column);

        for (T enumConstant : enumConstants) {
            if (raw.equals(enumConstant.name())) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException();
    }

    Ticket buildTicket(DbRow row) {
        Integer id = row.getInt("id");
        UUID player = getUUID(row, "uuid");
        ArrayList<Message> messages = null; // = row.getInt("id");
        TicketStatus status = getEnumValue(row, TicketStatus.class, "status");
        Location location = getLocation(row, "location");
        UUID picker = getUUID(row, "picker");

        return new Ticket(id, player, messages, status, location, picker);
    }

    Message buildMessage(DbRow row) {
        MessageReason reason = getEnumValue(row, MessageReason.class, "reason");
        LocalDateTime date = getDate(row, "date");

        String data = row.getString("data");

        if (data != null) {
            return new Message(reason, date, data);
        }

        UUID sender = getUUID(row, "sender");

        if (sender != null) {
            return new Message(reason, date, sender);
        }

        throw new IllegalArgumentException();
    }

    PendingNotification buildNotification(DbRow row) {
        Messages message = getEnumValue(row, Messages.class, "message");
        String[] replacements = row.getString("replacements").split("\\|");

        return new PendingNotification(message, replacements);
    }
}
