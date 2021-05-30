package broccolai.tickets.api.service.context;

import broccolai.corn.context.ContextKey;
import broccolai.corn.context.ContextKeyRegistry;
import broccolai.tickets.api.model.context.ContextMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ContextService {

    Function<ContextKey<?>, String> STRING_CONVERTER = key -> key.namespace() + ":" + key.name();

    BiFunction<String, String, String> STRING_BUILDER = (namespace, name) -> namespace + ":" + name;

    void registerKeys(@NonNull ContextKeyRegistry keyRegistry);

    <T> Optional<ContextKey<T>> parseKey(@NonNull String namespace, @NonNull String name);

    <T> void registerMapper(@NonNull ContextKey<T> key, @NonNull ContextMapper<T> mapper);

    <T> Optional<ContextMapper<T>> useMapper(@NonNull ContextKey<T> key);

}
