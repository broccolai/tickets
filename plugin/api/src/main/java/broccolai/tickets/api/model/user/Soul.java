package broccolai.tickets.api.model.user;

import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

public interface Soul {

    @Pure
    @NonNull UUID uuid();

    @Pure
    @NonNull String username();

}
