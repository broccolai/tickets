package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ActionCodec<T extends TicketAction> {

    static <T extends TicketAction> ActionCodec<T> of(
        final String type,
        final Class<T> actionClass,
        final Function<T, Map<String, Object>> encoder,
        final ActionDecoder<T> decoder
    ) {
        return new FunctionalActionCodec<>(type, actionClass, encoder, decoder);
    }

    String type();

    Class<T> actionClass();

    Map<String, Object> encode(T action);

    T decode(Instant date, UUID creator, ActionProperties properties);

    @FunctionalInterface
    interface ActionDecoder<T extends TicketAction> {

        T decode(Instant date, UUID creator, ActionProperties properties);
    }
}

@NullMarked
record FunctionalActionCodec<T extends TicketAction>(
    String type,
    Class<T> actionClass,
    Function<T, Map<String, Object>> encoder,
    ActionCodec.ActionDecoder<T> decoder
) implements ActionCodec<T> {

    @Override
    public Map<String, Object> encode(final T action) {
        return this.encoder.apply(action);
    }

    @Override
    public T decode(final Instant date, final UUID creator, final ActionProperties properties) {
        return this.decoder.decode(date, creator, properties);
    }
}
