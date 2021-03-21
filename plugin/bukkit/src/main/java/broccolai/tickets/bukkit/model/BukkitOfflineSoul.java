package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.user.Soul;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class BukkitOfflineSoul implements Soul {

    private final OfflinePlayer offlinePlayer;

    public BukkitOfflineSoul(final @NonNull OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.offlinePlayer.getUniqueId();
    }

    @Override
    public @NonNull String username() {
        String username = this.offlinePlayer.getName();

        if (username == null) {
            username = "UNKNOWN";
        }

        return username;
    }

}
