package love.broccolai.tickets.api.model.format;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public TicketFormat {
        parts = List.copyOf(parts);
    }

    public Optional<TicketFormatPart> findPart(final String identifier) {
        return this.parts.stream()
            .filter(part -> Objects.equals(part.identifier(), identifier))
            .findFirst();
    }

    public TicketFormatPart requirePart(final String identifier) {
        return this.findPart(identifier)
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket format part: " + identifier));
    }
}
