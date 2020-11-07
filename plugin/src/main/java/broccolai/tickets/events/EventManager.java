package broccolai.tickets.events;

import net.kyori.event.EventBus;
import net.kyori.event.SimpleEventBus;
import net.kyori.event.method.MethodSubscriptionAdapter;
import net.kyori.event.method.SimpleMethodSubscriptionAdapter;
import net.kyori.event.method.asm.ASMEventExecutorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class EventManager {

    private final EventBus<Event> bus = new SimpleEventBus<>(Event.class);
    private final MethodSubscriptionAdapter<EventListener> methodAdapter = new SimpleMethodSubscriptionAdapter<>(
            this.bus,
            new ASMEventExecutorFactory<>()
    );

    /**
     * Register any number of event listeners to the method adapter
     *
     * @param events Events to register
     */
    public void registerListeners(final @NonNull EventListener... events) {
        for (final EventListener listener : events) {
            this.methodAdapter.register(listener);
        }
    }

    /**
     * Call an event
     *
     * @param event Event to call
     */
    public void call(final @NonNull Event event) {
        this.bus.post(event);
    }

}
