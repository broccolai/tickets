package broccolai.tickets.utilities.generic;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utilities for Users.
 */
public class UserUtilities {
    /**
     * Convert a unique id to a readable username.
     *
     * @param uuid the users unique id
     * @return the users name
     */
    @NotNull
    public static String nameFromUUID(@Nullable UUID uuid) {
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
    public static UUID uuidFromSender(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId();
        } else {
            return null;
        }
    }
}
