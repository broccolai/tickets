package broccolai.tickets.api.model.context;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ContextMapper<T> {

    @SuppressWarnings("unchecked")
    default @Nullable String serializeObject(@Nullable Object value) {
        return this.serialize((T) value);
    }

    @Nullable String serialize(@Nullable T value);

    @Nullable T deserialize(@Nullable String value);

}
