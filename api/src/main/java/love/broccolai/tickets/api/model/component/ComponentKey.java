package love.broccolai.tickets.api.model.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record ComponentKey<T extends TicketComponent>(
    String value,
    Class<T> type
) {

    public static <T extends TicketComponent> ComponentKey<T> of(
        final String value,
        final Class<T> type
    ) {
        return new ComponentKey<>(value, type);
    }
}
