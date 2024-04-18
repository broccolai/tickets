package love.broccolai.tickets.api.registry;

import java.util.Collection;

public interface Registry<T> {
    void register(String identifier, T data);

    Collection<String> identifiers();

    T fromIdentifier(String identifier);

    String fromData(T data);
}
