package broccolai.tickets.api.service.event;

import broccolai.tickets.api.model.event.Event;
import broccolai.tickets.api.model.event.Subscriber;
import net.kyori.event.EventBus;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface EventService extends EventBus<Event> {

    void register(@NonNull Subscriber subscriber);

}
