package love.broccolai.tickets.api.model.format;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record TicketFormatPart(
    String identifier,
    TicketFormatStyle style
) {
}
