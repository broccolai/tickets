package broccolai.tickets.events;

import broccolai.tickets.exceptions.PureException;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that can be thrown (cancelled) with a message.
 */
class ThrowableEvent extends BaseEvent implements Cancellable {
    @Nullable
    private PureException exception;
    private boolean isCancelled;

    /**
     * Initialise Throwable Event.
     */
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

    /**
     * Cancel the event with a PureException.
     * @param exception the PureException to use.
     */
    public void cancel(@NotNull PureException exception) {
        this.exception = exception;
        isCancelled = true;
    }

    public boolean hasException() {
        return exception != null;
    }

    @NotNull
    public PureException getException() {
        assert exception != null;
        return exception;
    }
}
