package broccolai.tickets.api.service.event;

import broccolai.tickets.api.model.event.Event;
import net.kyori.event.EventSubscriber;
import net.kyori.event.PostResult;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface EventService {

    @NonNull PostResult post(@NonNull Event event);

    <T extends Event> void register(@NonNull Class<T> clazz, @NonNull EventSubscriber<T> subscriber);

}
