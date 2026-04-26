package love.broccolai.tickets.api.model.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ComponentProperties(
    Map<String, Object> values
) {

    public ComponentProperties {
        values = Collections.unmodifiableMap(new HashMap<>(values));
    }

    public <T> Optional<T> find(final String path, final Class<T> type) {
        return Optional.ofNullable(this.values.get(path))
            .map(type::cast);
    }

    public <T> T require(final String path, final Class<T> type) {
        return this.find(path, type)
            .orElseThrow(() -> new IllegalArgumentException("Missing component property: " + path));
    }
}
