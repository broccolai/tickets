package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.tasks.TaskService;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
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
    public boolean permission(final @NonNull String permission) {
        return this.player.getPlayer().hasPermission(permission);
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

    @Override
    public @NonNull Position position() {
        Location location = this.player.getLocation();
        World world = location.getWorld();
        String worldName = world != null ? world.getName() : null;

        return new Position(worldName, (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    @Override
    public void teleport(final @NonNull TaskService taskService, final @NonNull Position position) {
        World world = position.world() != null ? Bukkit.getWorld(position.world()) : null;
        Location bukkitLocation = new Location(world, position.x(), position.y(), position.z());

        taskService.sync(() -> {
            PaperLib.teleportAsync(this.player, bukkitLocation);
        });
    }

    @Override
    public CommandSender sender() {
        return this.player;
    }

}
