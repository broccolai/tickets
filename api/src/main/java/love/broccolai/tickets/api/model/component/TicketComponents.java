package love.broccolai.tickets.api.model.component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TicketComponents(
    Map<ComponentKey<?>, TicketComponent> values
) {

    public static final TicketComponents EMPTY = new TicketComponents(Map.of());

    public TicketComponents {
        values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    public <T extends TicketComponent> Optional<T> find(final ComponentKey<T> key) {
        return Optional.ofNullable(this.values.get(key))
            .map(key.type()::cast);
    }

    public <T extends TicketComponent> T require(final ComponentKey<T> key) {
        return this.find(key)
            .orElseThrow(() -> new IllegalArgumentException("Missing ticket component: " + key.value()));
    }

    public TicketComponents with(final TicketComponent component) {
        Map<ComponentKey<?>, TicketComponent> updatedValues = new LinkedHashMap<>(this.values);
        updatedValues.put(component.key(), component);

        return new TicketComponents(updatedValues);
    }

    public TicketComponents without(final ComponentKey<?> key) {
        Map<ComponentKey<?>, TicketComponent> updatedValues = new LinkedHashMap<>(this.values);
        updatedValues.remove(key);

        return new TicketComponents(updatedValues);
    }
}
