package love.broccolai.tickets.api.model.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

//todo: improve
public final class TicketFormatContent {

    private final String formatIdentifier;
    private final Map<String, Object> parts = new HashMap<>();

    public TicketFormatContent(final String formatIdentifier) {
        this.formatIdentifier = formatIdentifier;
    }

    public String formatIdentifier() {
        return this.formatIdentifier;
    }

    public <T> void put(final String identifier, final T value) {
        this.parts.put(identifier, value);
    }

    public <T> T get(final String identifier) {
        return (T) this.parts.get(identifier);
    }

    public void forEach(final BiConsumer<String, Object> consumer) {
        this.parts.forEach(consumer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketFormatContent that)) {
            return false;
        }

        if (!Objects.equals(this.formatIdentifier, that.formatIdentifier)) {
            return false;
        }

        return Objects.equals(this.parts, that.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.formatIdentifier, this.parts);
    }
}
