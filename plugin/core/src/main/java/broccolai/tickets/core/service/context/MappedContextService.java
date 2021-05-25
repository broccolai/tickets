package broccolai.tickets.core.service.context;

import broccolai.corn.context.ContextKey;
import broccolai.corn.context.ContextKeyRegistry;
import broccolai.tickets.api.model.context.ContextMapper;
import broccolai.tickets.api.service.context.ContextService;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public final class MappedContextService implements ContextService {

    private final Map<String, ContextKey<?>> namedContexts = new HashMap<>();
    private final Map<ContextKey<?>, ContextMapper<?>> contextMappers = new HashMap<>();

    @Override
    public void registerKeys(@NotNull final ContextKeyRegistry keyRegistry) {
        keyRegistry.forEach(key -> this.namedContexts.put(STRING_BUILDER.apply(key), key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<ContextKey<T>> parseKey(final @NonNull String keyString) {
        ContextKey<T> key = (ContextKey<T>) this.namedContexts.get(keyString);
        return Optional.ofNullable(key);
    }

    @Override
    public <T> void registerMapper(@NotNull final ContextKey<T> key, final @NonNull ContextMapper<T> mapper) {
        this.contextMappers.put(key, mapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<ContextMapper<T>> useMapper(@NotNull final ContextKey<T> key) {
        ContextMapper<T> mapper = (ContextMapper<T>) this.contextMappers.get(key);
        return Optional.ofNullable(mapper);
    }

}
