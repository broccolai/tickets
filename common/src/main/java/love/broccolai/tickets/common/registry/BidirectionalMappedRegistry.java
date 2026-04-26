package love.broccolai.tickets.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import love.broccolai.tickets.api.registry.Registry;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public abstract class BidirectionalMappedRegistry<T> implements Registry<T> {

    private final BiMap<String, T> storage = HashBiMap.create();

    @Override
    public void register(final String identifier, final T data) {
        this.storage.put(identifier, data);
    }

    @Override
    public Collection<String> identifiers() {
        return List.copyOf(this.storage.keySet());
    }

    @Override
    public Optional<T> findByIdentifier(final String identifier) {
        return Optional.ofNullable(this.storage.get(identifier));
    }

    @Override
    public Optional<String> findIdentifier(final T type) {
        return Optional.ofNullable(this.storage.inverse().get(type));
    }
}
