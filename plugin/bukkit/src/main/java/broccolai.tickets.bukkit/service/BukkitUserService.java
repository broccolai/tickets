package broccolai.tickets.bukkit.service;

import broccolai.tickets.bukkit.model.User.BukkitPlayerUserAudience;
import broccolai.tickets.core.model.user.ConsoleUserAudience;
import broccolai.tickets.core.model.user.PlayerUserAudience;
import broccolai.tickets.core.service.impl.SimpleUserService;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class BukkitUserService extends SimpleUserService<CommandSender, Player> {

    public BukkitUserService(final @NonNull AudienceProvider audienceProvider) {
        super(audienceProvider);
    }

    @Override
    public @NonNull PlayerUserAudience playerAudience(@NonNull final Player player) {
        return new BukkitPlayerUserAudience(player, this.audienceProvider.player(player.getUniqueId()));
    }

    @Override
    public @NonNull UUID uuid(@NonNull final CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return ConsoleUserAudience.UUID;
        }

        return this.player(sender).getUniqueId();
    }

    @Override
    public @NonNull Player player(@NonNull final CommandSender sender) {
        return (Player) sender;
    }

}
