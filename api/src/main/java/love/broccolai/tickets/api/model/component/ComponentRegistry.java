package love.broccolai.tickets.api.model.component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ComponentRegistry {

    Collection<ComponentCodec<?>> codecs();

    Optional<ComponentCodec<?>> find(String key);

    void register(ComponentCodec<?> codec);

    default <T extends TicketComponent> Optional<ComponentCodec<T>> find(final ComponentKey<T> key) {
        return this.find(key.value())
            .map(codec -> this.cast(key, codec));
    }

    default ComponentCodec<?> require(final String key) {
        return this.find(key)
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket component: " + key));
    }

    default <T extends TicketComponent> ComponentCodec<T> require(final ComponentKey<T> key) {
        return this.find(key)
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket component: " + key.value()));
    }

    default Map<String, Object> encode(final TicketComponent component) {
        ComponentCodec<?> codec = this.require(component.key().value());

        return this.encode(codec, component);
    }

    default Optional<TicketComponent> decode(final String key, final ComponentProperties properties) {
        return this.find(key)
            .map(codec -> this.decode(codec, properties));
    }

    default TicketComponent requireDecoded(final String key, final ComponentProperties properties) {
        return this.decode(key, properties)
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket component: " + key));
    }

    private <T extends TicketComponent> ComponentCodec<T> cast(
        final ComponentKey<T> key,
        final ComponentCodec<?> codec
    ) {
        if (!codec.key().type().equals(key.type())) {
            throw new IllegalArgumentException("Component key type mismatch: " + key.value());
        }

        @SuppressWarnings("unchecked")
        ComponentCodec<T> typedCodec = (ComponentCodec<T>) codec;

        return typedCodec;
    }

    private <T extends TicketComponent> Map<String, Object> encode(
        final ComponentCodec<T> codec,
        final TicketComponent component
    ) {
        return codec.encode(codec.key().type().cast(component));
    }

    private <T extends TicketComponent> TicketComponent decode(
        final ComponentCodec<T> codec,
        final ComponentProperties properties
    ) {
        return codec.decode(properties);
    }
}
