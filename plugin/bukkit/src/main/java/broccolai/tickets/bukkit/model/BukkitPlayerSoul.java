package broccolai.tickets.bukkit.model;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.tasks.TaskService;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public final class BukkitPlayerSoul implements PlayerSoul, OnlineSoul {

    private final UUID uuid;
    private final Audience audience;

    public BukkitPlayerSoul(final @NonNull UUID uuid, final @NonNull Audience audience) {
        this.uuid = uuid;
        this.audience = audience;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    @Override
    public @NonNull String username() {
        return Bukkit.getPlayer(this.uuid).getName();
    }

    @Override
    public boolean permission(final @NonNull String permission) {
        return Bukkit.getPlayer(this.uuid).hasPermission(permission);
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

    @Override
    public @NonNull Position position() {
        Location location = Bukkit.getPlayer(this.uuid).getLocation();
        World world = location.getWorld();
        String worldName = world != null ? world.getName() : null;

        return new Position(worldName, (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    @Override
    public void teleport(final @NonNull TaskService taskService, final @NonNull Position position) {
        World world = position.world() != null ? Bukkit.getWorld(position.world()) : null;
        Location bukkitLocation = new Location(world, position.x(), position.y(), position.z());

        taskService.sync(() -> {
            PaperLib.teleportAsync(Bukkit.getPlayer(this.uuid), bukkitLocation);
        });
    }

}
