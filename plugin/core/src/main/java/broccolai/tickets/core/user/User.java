package broccolai.tickets.core.user;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.UUID;

public interface User {

    /**
     * Get the souls name
     *
     * @return Name
     */
    @NonNull String getName();

    /**
     * Get the souls unique id
     *
     * @return Unique id
     */
    @NonNull UUID getUniqueId();

    class Simple implements User {

        private final UUID uuid;
        private final String name;

        Simple(final @NonNull UUID uuid, final @NonNull String name) {
            this.uuid = uuid;
            this.name = name;
        }

        @Override
        public @NonNull String getName() {
            return name;
        }

        @Override
        public @NonNull UUID getUniqueId() {
            return uuid;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Simple simple = (Simple) o;
            return Objects.equals(this.getUniqueId(), simple.getUniqueId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getUniqueId());
        }

    }
}
