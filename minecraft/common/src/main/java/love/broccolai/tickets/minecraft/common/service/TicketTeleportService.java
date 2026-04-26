package love.broccolai.tickets.minecraft.common.service;

import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TicketTeleportService {

    boolean teleport(PlayerCommander commander, Location location);
}
