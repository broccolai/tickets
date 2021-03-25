package broccolai.tickets.sponge7.model;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.tasks.TaskService;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public final class SpongePlayerSoul implements PlayerSoul, SpongeOnlineSoul {

    private final Player player;
    private final Audience audience;

    public SpongePlayerSoul(final @NonNull Player player, final @NonNull Audience audience) {
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
        return this.player.hasPermission(permission);
    }

    @Override
    public @NonNull Audience audience() {
        return this.audience;
    }

    @Override
    public @NonNull Position position() {
        Location<World> location = this.player.getLocation();
        World world = location.getExtent();

        return new Position(world.getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    @Override
    public void teleport(final @NonNull TaskService taskService, final @NonNull Position position) {
        World world = Sponge.getServer().getWorld(position.world()).orElse(null);
        Location<World> location = new Location<>(world, position.x(), position.y(), position.z());

        this.player.setLocation(location);
    }

    @Override
    public CommandSource sender() {
        return this.player;
    }

}
