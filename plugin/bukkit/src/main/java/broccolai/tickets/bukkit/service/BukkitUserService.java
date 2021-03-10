package broccolai.tickets.bukkit.service;

import broccolai.corn.core.Lists;
import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.bukkit.model.BukkitPlayerSoul;
import broccolai.tickets.core.service.user.SimpleUserService;

import com.google.inject.Inject;

import com.google.inject.Singleton;

import java.util.Collection;

import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

@Singleton
public final class BukkitUserService extends SimpleUserService<CommandSender, Player> {

    @Inject
    public BukkitUserService(final @NonNull AudienceProvider audienceProvider) {
        super(audienceProvider);
    }

    @Override
    public @NonNull PlayerSoul player(@NonNull final Player player) {
        return new BukkitPlayerSoul(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull UUID uuid(@NonNull final CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return ConsoleSoul.UUID;
        }

        Player player = (Player) sender;
        return player.getUniqueId();
    }

    @Override
    public @NonNull CommandSender sender(@NonNull final UUID uuid) {
        if (uuid == ConsoleSoul.UUID) {
            return Bukkit.getConsoleSender();
        }

        return Bukkit.getPlayer(uuid);
    }

    @Override
    public @NonNull Collection<PlayerSoul> players() {
        return Lists.map(Bukkit.getOnlinePlayers(), this::player);
    }

}
