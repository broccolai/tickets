package broccolai.tickets.bukkit.user;

import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.utilities.TicketLocation;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class BukkitPlayerSoul extends PlayerSoul<CommandSender, Player> {

    /**
     * Construct a PlayerSoul with a Player instance
     *
     * @param userManager User manager
     * @param audience    Adventure audience
     * @param player      Player instance
     */
    public BukkitPlayerSoul(
            final @NonNull BukkitUserManager userManager,
            final @NonNull Audience audience,
            final @NonNull Player player
    ) {
        super(userManager, audience, player);
    }

    @Override
    public @NonNull String getName() {
        return this.player.getName();
    }

    @Override
    public @NonNull UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public boolean hasPermission(@NonNull final String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public @NonNull TicketLocation currentLocation() {
        Location location = this.player.getLocation();
        String world = location.getWorld() != null ? location.getWorld().getName() : null;

        return new TicketLocation(world, location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void teleport(@NonNull final TicketLocation location) {
        //todo: fix with guice
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("PureTickets"), () -> {
            World bukkitWorld = Bukkit.getWorld(location.getWorld());
            Location bukkitLocation = new Location(bukkitWorld, location.getX(), location.getY(), location.getZ());
            PaperLib.teleportAsync(this.player, bukkitLocation);
        });
    }

}
