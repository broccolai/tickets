package broccolai.tickets.api.model.user;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.UUID;

public interface Soul{
    @Pure
    @NonNull UUID uuid();
}
