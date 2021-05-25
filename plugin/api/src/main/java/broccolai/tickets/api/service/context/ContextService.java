package broccolai.tickets.api.service.context;

import broccolai.corn.context.ContextKey;
import broccolai.corn.context.ContextKeyRegistry;
import broccolai.tickets.api.model.context.ContextMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Optional;
import java.util.function.Function;

public interface ContextService {

    Function<ContextKey<?>, String> STRING_BUILDER = key -> key.namespace() + ":" + key.name();

    void registerKeys(@NonNull ContextKeyRegistry keyRegistry);

    <T> Optional<ContextKey<T>> parseKey(@NonNull String keyString);

    <T> void registerMapper(@NonNull ContextKey<T> key, final @NonNull ContextMapper<T> mapper);

    <T> Optional<ContextMapper<T>> useMapper(@NonNull ContextKey<T> key);

}
