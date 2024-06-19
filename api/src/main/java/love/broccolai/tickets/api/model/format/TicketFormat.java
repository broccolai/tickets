package love.broccolai.tickets.api.model.format;

import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
public record TicketFormat(
    String identifier,
    String displayName,
    String description,
    List<TicketFormatPart> parts
) {

}
