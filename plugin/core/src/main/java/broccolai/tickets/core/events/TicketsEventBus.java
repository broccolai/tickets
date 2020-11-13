package broccolai.tickets.core.events;

import net.kyori.event.SimpleEventBus;
import net.kyori.event.method.MethodSubscriptionAdapter;
import net.kyori.event.method.SimpleMethodSubscriptionAdapter;
import net.kyori.event.method.asm.ASMEventExecutorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketsEventBus extends SimpleEventBus<Event> {

    private final MethodSubscriptionAdapter<EventListener> methodAdapter = new SimpleMethodSubscriptionAdapter<>(
            this,
            new ASMEventExecutorFactory<>()
    );

    /**
     * Create a new ticket event bus
     */
    public TicketsEventBus() {
        super(Event.class);
    }

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

}
