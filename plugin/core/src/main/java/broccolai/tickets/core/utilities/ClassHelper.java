package broccolai.tickets.core.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ClassHelper {

    private ClassHelper() {
        // utility class
    }

    public static boolean classExists(final @NonNull String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

}
