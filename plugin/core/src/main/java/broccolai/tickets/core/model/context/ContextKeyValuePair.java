package broccolai.tickets.core.model.context;

import broccolai.corn.context.ContextKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ContextKeyValuePair<T> {

    private final ContextKey<T> key;
    private final T value;

    public ContextKeyValuePair(final @NonNull ContextKey<@NonNull T> key, final @Nullable T value) {
        this.key = key;
        this.value = value;
    }

    public ContextKey<T> key() {
        return this.key;
    }

    public T value() {
        return this.value;
    }

}
