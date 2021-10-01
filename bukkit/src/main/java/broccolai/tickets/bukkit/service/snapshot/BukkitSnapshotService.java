package broccolai.tickets.bukkit.service.snapshot;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.google.inject.Singleton;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Singleton
@Order(ExecutionOrder.LATE)
public final class BukkitSnapshotService implements SoulSnapshotService {

    @Override
    public @Nullable SoulSnapshot handleUniqueId(final @NonNull UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.getName() != null) {
            return new SoulSnapshot(player.getUniqueId(), player.getName());
        }

        return null;
    }

    @Override
    public @Nullable SoulSnapshot handleName(final @NonNull String name) {
        Player player = Bukkit.getServer().getPlayerExact(name);
        if (player != null) {
            return new SoulSnapshot(player.getUniqueId(), player.getName());
        }

        return null;
    }

}
