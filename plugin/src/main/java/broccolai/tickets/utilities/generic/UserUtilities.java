package broccolai.tickets.utilities.generic;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
