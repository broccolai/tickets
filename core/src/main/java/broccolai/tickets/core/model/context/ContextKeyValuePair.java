package broccolai.tickets.core.model.context;

import broccolai.corn.context.ContextKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public record ContextKeyValuePair<T>(@NonNull ContextKey<T> key, @Nullable T value) {

}
