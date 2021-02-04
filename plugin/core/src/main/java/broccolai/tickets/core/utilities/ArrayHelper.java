package broccolai.tickets.core.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ArrayHelper {
    private ArrayHelper() {
        // helper class
    }

    @SafeVarargs
    public static <T> @NonNull T @NonNull [] create(final @NonNull T... values) {
        return values;
    }
}
