package broccolai.tickets.utilities;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Utilities for Users
 */
public final class UserUtilities {

    private UserUtilities() {
    }

    /**
     * Convert a unique id to a readable username.
     *
     * @param uuid the users unique id
     * @return the users name
     */
    @NonNull
    public static String nameFromUUID(@Nullable final UUID uuid) {
        if (uuid == null) {
            return Bukkit.getConsoleSender().getName();
        } else {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            return name != null ? name : "unknown";
        }
    }

    /**
     * Convert a command sender to a unique id.
     *
     * @param sender the command sender
     * @return an unique id representing them
     */
    @Nullable
    public static UUID uuidFromSender(@NonNull final CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId();
        } else {
            return null;
        }
    }

}
