package broccolai.tickets.api.model.user;

import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public interface Soul {

    @Pure
    @NonNull UUID uuid();

    @Pure
    @NonNull String username();

    abstract class Abstract implements Soul {

        @Override
        public boolean equals(final @Nullable Object obj) {
            if (!(obj instanceof Soul otherSoul)) {
                return false;
            }

            return this.uuid().equals(otherSoul.uuid());
        }

        @Override
        public int hashCode() {
            return this.uuid().hashCode();
        }

    }

}
