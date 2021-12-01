package broccolai.tickets.api.model.user;

import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public interface User {

    @Pure
    @NonNull UUID uuid();

    @Pure
    @NonNull String username();

}
