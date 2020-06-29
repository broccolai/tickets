package co.uk.magmo.puretickets.utilities;

import org.bukkit.Bukkit;

import java.util.UUID;

public class UserUtilities {
    public static String nameFromUUID(UUID uuid) {
        if (uuid == null) {
            return Bukkit.getConsoleSender().getName();
        } else {
            return Bukkit.getOfflinePlayer(uuid).getName();
        }
    }
}
