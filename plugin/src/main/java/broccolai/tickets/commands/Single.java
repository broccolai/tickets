package broccolai.tickets.commands;

import cloud.commandframework.types.tuples.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Single<T> implements Tuple {
    @Nullable
    final T value;

    Single(@Nullable final T value) {
        this.value = value;
    }

    public static <T> Single<T> of(T value) {
        return new Single<>(value);
    }

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
