package broccolai.tickets.commands;

import cloud.commandframework.types.tuples.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Single<T> implements Tuple {

    @Nullable
    private final T value;

    private Single(@Nullable final T value) {
        this.value = value;
    }

    /**
     * Create a new 1-tuple
     *
     * @param value Value
     * @param <T>   Value type
     * @return Created pair
     */
    @NotNull
    public static <T> Single<T> of(@Nullable final T value) {
        return new Single<>(value);
    }

    /**
     * Get the value
     *
     * @return Value
     */
    @Nullable
    public T get() {
        return value;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        final Object[] array = new Object[1];
        array[0] = this.value;
        return array;
    }

}
