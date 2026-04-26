package love.broccolai.tickets.minecraft.paper;

import com.google.inject.Inject;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.service.TicketTeleportService;
import love.broccolai.tickets.minecraft.paper.model.PaperPlayerCommander;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperTicketTeleportService implements TicketTeleportService {

    private final Plugin plugin;

    @Inject
    public PaperTicketTeleportService(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean teleport(final PlayerCommander commander, final Location location) {
        World world = Bukkit.getWorld(location.world());

        if (world == null || !(commander instanceof PaperPlayerCommander playerCommander)) {
            return false;
        }

        var destination = playerCommander.player().getLocation();
        destination.setWorld(world);
        destination.setX(location.x());
        destination.setY(location.y());
        destination.setZ(location.z());
        destination.setYaw(location.yaw());
        destination.setPitch(location.pitch());

        Bukkit.getScheduler().runTask(this.plugin, () -> playerCommander.player().teleport(destination));
        return true;
    }
}
