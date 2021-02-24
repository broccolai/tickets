package broccolai.tickets.bukkit.model.User;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.core.utilities.TicketLocation;

import io.papermc.lib.PaperLib;

import java.util.UUID;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitPlayerSoul implements PlayerSoul, BukkitOnlineSoul {

    private final Player player;
    private final Audience audience;

    public BukkitPlayerSoul(final @NonNull Player player, final @NonNull Audience audience) {
        this.player = player;
        this.audience = audience;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public @NonNull String username() {
        return this.player.getName();
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

    //todo
    public @NonNull TicketLocation location() {
        Location location = this.player.getLocation();
        World world = location.getWorld();
        String worldName = world != null ? world.getName() : null;

        return new TicketLocation(worldName, location.getX(), location.getY(), location.getZ());
    }

    //todo
    public void teleport(final @NonNull TicketLocation location) {
        World world = Bukkit.getWorld(location.getWorld());
        Location bukkitLocation = new Location(world, location.getX(), location.getY(), location.getZ());
        PaperLib.teleportAsync(this.player, bukkitLocation);
    }

    @Override
    public @NonNull CommandSender sender() {
        return this.player;
    }

    @Override
    public @NonNull Position position() {
        return null;
    }

    @Override
    public void teleport(@NonNull final Position location) {

    }

}
