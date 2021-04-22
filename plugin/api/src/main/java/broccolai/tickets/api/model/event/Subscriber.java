package broccolai.tickets.api.model.event;

import broccolai.tickets.api.service.event.EventService;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Subscriber {

    void register(@NonNull EventService eventService);

}
