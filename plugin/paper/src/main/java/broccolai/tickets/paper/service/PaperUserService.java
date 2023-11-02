package broccolai.tickets.paper.service;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.paper.model.PaperConsoleSoul;
import broccolai.tickets.paper.model.PaperOfflineSoul;
import broccolai.tickets.paper.model.PaperPlayerSoul;
import broccolai.tickets.core.service.user.SimpleUserService;
import com.google.inject.Singleton;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Collection;
import java.util.UUID;

@Singleton
public final class PaperUserService extends SimpleUserService {

    public @NonNull PlayerSoul player(final @NonNull Player player) {
        return new PaperPlayerSoul(player);
    }

    @Override
    public @NonNull ConsoleSoul console() {
        return new PaperConsoleSoul(Bukkit.getConsoleSender());
    }

    @Override
    public @NonNull PlayerSoul player(final @NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            throw new IllegalArgumentException();
        }

        return new PaperPlayerSoul(player);
    }

    @Override
    public @NonNull Soul offlinePlayer(final @NonNull UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        return new PaperOfflineSoul(offlinePlayer);
    }

    @Override
    public @NonNull Collection<PlayerSoul> players() {
        Collection<PlayerSoul> players = new ArrayList<>();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            players.add(this.player(player.getUniqueId()));
        }

        return players;
    }

    @Override
    public @NonNull String name(final @NonNull UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name == null ? "UNKNOWN" : name;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NonNull UUID uuidFromName(final @NonNull String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    @Override
    protected boolean isOnline(final @NonNull UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).isOnline();
    }

}
