package broccolai.tickets.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Base event for other events to extend.
 */
class BaseEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Initialise BaseEvent with isAsync.
     */
    public BaseEvent(boolean isAsync) {
        super(isAsync);
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
