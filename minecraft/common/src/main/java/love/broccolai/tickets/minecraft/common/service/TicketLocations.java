package love.broccolai.tickets.minecraft.common.service;

import java.util.Optional;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.Ticket;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TicketLocations {

    public Optional<Location> find(final Ticket ticket) {
        return ticket.form().parts()
            .values()
            .stream()
            .filter(Location.class::isInstance)
            .map(Location.class::cast)
            .findFirst();
    }
}
