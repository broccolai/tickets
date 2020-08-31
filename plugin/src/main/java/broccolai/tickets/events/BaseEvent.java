package broccolai.tickets.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

class BaseEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

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
