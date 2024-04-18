package love.broccolai.tickets.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Singleton;
import java.util.Collection;
import love.broccolai.tickets.api.registry.Registry;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public abstract class BidirectionalMappedRegistry<T> implements Registry<T> {

    private final BiMap<String, T> storage = HashBiMap.create();

    @Override
    public void register(String identifier, T data) {
        this.storage.put(identifier, data);
    }

    @Override
    public Collection<String> identifiers() {
        return this.storage.keySet();
    }

    @Override
    public T fromIdentifier(String identifier) {
        return this.storage.get(identifier);
    }

    public String fromData(T type) {
        return this.storage.inverse().get(type);
    }
}
