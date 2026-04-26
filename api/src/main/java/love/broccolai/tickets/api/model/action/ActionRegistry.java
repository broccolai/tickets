package love.broccolai.tickets.api.model.action;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ActionRegistry {

    Collection<ActionCodec<?>> codecs();

    Optional<ActionCodec<?>> find(String type);

    void register(ActionCodec<?> codec);

    default ActionCodec<?> require(final String type) {
        return this.find(type)
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket action type: " + type));
    }

    default String type(final TicketAction action) {
        Optional<String> exactType = this.codecs()
            .stream()
            .filter(codec -> codec.actionClass().equals(action.getClass()))
            .map(ActionCodec::type)
            .findFirst();

        if (exactType.isPresent()) {
            return exactType.get();
        }

        return this.codecs()
            .stream()
            .filter(codec -> codec.actionClass().isInstance(action))
            .map(ActionCodec::type)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown ticket action: " + action.getClass().getName()));
    }

    default Map<String, Object> encode(final TicketAction action) {
        ActionCodec<?> codec = this.require(this.type(action));
        return this.encode(codec, action);
    }

    default TicketAction decode(
        final String type,
        final Instant date,
        final UUID creator,
        final ActionProperties properties
    ) {
        return this.decode(this.require(type), date, creator, properties);
    }

    private <T extends TicketAction> Map<String, Object> encode(
        final ActionCodec<T> codec,
        final TicketAction action
    ) {
        return codec.encode(codec.actionClass().cast(action));
    }

    private <T extends TicketAction> TicketAction decode(
        final ActionCodec<T> codec,
        final Instant date,
        final UUID creator,
        final ActionProperties properties
    ) {
        return codec.decode(date, creator, properties);
    }
}
