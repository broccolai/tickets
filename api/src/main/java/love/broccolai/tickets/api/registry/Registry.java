package love.broccolai.tickets.api.registry;

import java.util.Collection;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Registry<T> {

    void register(String identifier, T data);

    Collection<String> identifiers();

    Optional<T> findByIdentifier(String identifier);

    Optional<String> findIdentifier(T data);

    default T requireIdentifier(String identifier) {
        return this.findByIdentifier(identifier)
            .orElseThrow(() -> new IllegalArgumentException("Unknown registry identifier: " + identifier));
    }

    default String requireData(T data) {
        return this.findIdentifier(data)
            .orElseThrow(() -> new IllegalArgumentException("Unregistered registry data: " + data));
    }
}
