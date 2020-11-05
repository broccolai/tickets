package broccolai.tickets.utilities;

import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public final class UserUtilities {

    private UserUtilities() {
    }

    /**
     * Convert a unique id to a readable username
     *
     * @param uuid Users unique id
     * @return Users name
     */
    public static @NonNull String nameFromUUID(final @Nullable UUID uuid) {
        if (uuid == null) {
            return Bukkit.getConsoleSender().getName();
        } else {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            return name != null ? name : "unknown";
        }
    }

}
