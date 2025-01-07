package love.broccolai.tickets.api.model.format;

import java.util.List;
import java.util.Objects;
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

    public TicketFormatPart findPart(final String identifier) {
        return this.parts.stream()
            .filter(part -> Objects.equals(part.identifier(), identifier))
            .findFirst()
            .orElseThrow();
    }

}
