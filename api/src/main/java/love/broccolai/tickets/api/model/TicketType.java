package love.broccolai.tickets.api.model;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
public record TicketType(
    String identifier,
    String displayName,
    String description
) {
}
