package love.broccolai.tickets.common.service.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PropertyBag(
    Map<String, PropertyValue> values
) {

    public PropertyBag {
        values = Collections.unmodifiableMap(new HashMap<>(values));
    }

    public static PropertyBag of(final Map<String, Object> values) {
        Map<String, PropertyValue> encoded = new HashMap<>();
        values.forEach((path, value) -> encoded.put(path, PropertyValue.of(value)));

        return new PropertyBag(encoded);
    }

    public Map<String, Object> decoded() {
        Map<String, Object> decoded = new HashMap<>();
        this.values.forEach((path, value) -> decoded.put(path, value.value()));

        return decoded;
    }
}
