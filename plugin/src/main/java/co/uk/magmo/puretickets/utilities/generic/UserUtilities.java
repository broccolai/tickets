package co.uk.magmo.puretickets.utilities.generic;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserUtilities {
    public static String nameFromUUID(UUID uuid) {
        if (uuid == null) {
            return Bukkit.getConsoleSender().getName();
        } else {
            return Bukkit.getOfflinePlayer(uuid).getName();
        }
    }

    public static UUID uuidFromSender(CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId();
        } else {
            return null;
        }
    }
}
