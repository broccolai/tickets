package broccolai.tickets.core.service.event;

import broccolai.tickets.api.model.event.Event;
import broccolai.tickets.api.service.event.EventService;
import com.google.inject.Singleton;
import net.kyori.event.EventBus;
import net.kyori.event.EventSubscriber;
import net.kyori.event.PostResult;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class KyoriEventService implements EventService {

    private final EventBus<Event> eventBus = EventBus.create(Event.class);

    @Override
    public PostResult post(@NonNull final Event event) {
        return this.eventBus.post(event);
    }

    @Override
    public <T extends Event> void register(final @NonNull Class<T> clazz, final @NonNull EventSubscriber<T> subscriber) {
        this.eventBus.subscribe(clazz, subscriber);
    }

}
