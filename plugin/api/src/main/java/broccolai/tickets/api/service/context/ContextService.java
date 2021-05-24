package broccolai.tickets.api.service.context;

import broccolai.corn.context.ContextKey;
import broccolai.corn.context.ContextKeyRegistry;
import broccolai.tickets.api.model.context.ContextMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Optional;

public interface ContextService {

    void registerKeys(final @NonNull ContextKeyRegistry keyRegistry);

    <T> ContextKey<T> parseKey(final @NonNull String keyString);

    <T> void registerMapper(final @NonNull ContextKey<T> key, final @NonNull ContextMapper<T> mapper);

    <T> Optional<ContextMapper<T>> getMapper(final @NonNull ContextKey<T> key);

}
