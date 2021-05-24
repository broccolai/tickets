package broccolai.tickets.api.model.context;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ContextMapper<T> {

    @Nullable String serialize(final @Nullable T value);

    @Nullable T deserialize(final @Nullable T value);

}
