package broccolai.tickets.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Base event for other events to extend.
 */
class BaseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Initialise BaseEvent with isAsync.
     *
     * @param isAsync whether the event is ran async
     */
    BaseEvent(final boolean isAsync) {
        super(isAsync);
    }

    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
