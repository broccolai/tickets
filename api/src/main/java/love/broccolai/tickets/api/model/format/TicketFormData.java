package love.broccolai.tickets.api.model.format;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketFormData(
    String formatIdentifier,
    Map<String, Object> parts
) {

    public TicketFormData {
        parts = Collections.unmodifiableMap(new LinkedHashMap<>(parts));
    }

    public static TicketFormData empty(final String formatIdentifier) {
        return new TicketFormData(formatIdentifier, Map.of());
    }

    public <T> TicketFormData with(final String identifier, final T value) {
        Map<String, Object> updated = new LinkedHashMap<>(this.parts);
        updated.put(identifier, value);

        return new TicketFormData(this.formatIdentifier, updated);
    }

    public <T> Optional<T> find(final String identifier, final Class<T> type) {
        return Optional.ofNullable(this.parts.get(identifier))
            .map(type::cast);
    }

    public <T> T require(final String identifier, final Class<T> type) {
        return this.find(identifier, type)
            .orElseThrow(() -> new IllegalArgumentException("Missing ticket form data part: " + identifier));
    }
}
