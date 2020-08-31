package broccolai.tickets.events;

import broccolai.tickets.exceptions.PureException;
import org.bukkit.event.Cancellable;

class ThrowableEvent extends BaseEvent implements Cancellable {
    private PureException exception;
    private boolean isCancelled;

    public ThrowableEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    public void cancel(PureException exception) {
        this.exception = exception;
        isCancelled = true;
    }

    public boolean hasException() {
        return exception != null;
    }

    public PureException getException() {
        return exception;
    }
}
