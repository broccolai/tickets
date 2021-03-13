package broccolai.tickets.core.service.event;

import broccolai.tickets.api.model.event.Event;
import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.service.event.EventService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.event.SimpleEventBus;
import net.kyori.event.method.MethodSubscriptionAdapter;
import net.kyori.event.method.SimpleMethodSubscriptionAdapter;
import net.kyori.event.method.asm.ASMEventExecutorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class ASMEventService extends SimpleEventBus<Event> implements EventService {

    private final MethodSubscriptionAdapter<Subscriber> methodAdapter = new SimpleMethodSubscriptionAdapter<>(
            this,
            new ASMEventExecutorFactory<>()
    );

    @Inject
    public ASMEventService() {
        super(Event.class);
    }

    @Override
    public void register(final @NonNull Subscriber subscriber) {
        this.methodAdapter.register(subscriber);
    }

}
