package love.broccolai.tickets.api.model.component;

import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ComponentCodec<T extends TicketComponent> {

    static <T extends TicketComponent> ComponentCodec<T> of(
        final ComponentKey<T> key,
        final Function<T, Map<String, Object>> encoder,
        final Function<ComponentProperties, T> decoder
    ) {
        return new FunctionalComponentCodec<>(key, encoder, decoder);
    }

    ComponentKey<T> key();

    Map<String, Object> encode(T component);

    T decode(ComponentProperties properties);
}

@NullMarked
record FunctionalComponentCodec<T extends TicketComponent>(
    ComponentKey<T> key,
    Function<T, Map<String, Object>> encoder,
    Function<ComponentProperties, T> decoder
) implements ComponentCodec<T> {

    @Override
    public Map<String, Object> encode(final T component) {
        return this.encoder.apply(component);
    }

    @Override
    public T decode(final ComponentProperties properties) {
        return this.decoder.apply(properties);
    }
}
