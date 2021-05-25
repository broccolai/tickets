package broccolai.tickets.api.model.context;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ContextMapper<T> {

    @Nullable String serialize(@Nullable T value);

    @Nullable T deserialize(@Nullable String value);

}
