package broccolai.tickets.events;

import broccolai.tickets.exceptions.PureException;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that can be thrown (cancelled) with a message
 */
class ThrowableEvent extends BaseEvent implements Cancellable {

    @Nullable
    private PureException exception = null;
    private boolean isCancelled = false;

    /**
     * Initialise Throwable Event
     *
     * @param isAsync whether the event is ran async
     */
    ThrowableEvent(final boolean isAsync) {
        super(isAsync);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        isCancelled = cancel;
    }

    /**
     * Cancel the event with a PureException
     *
     * @param exception the PureException to use
     */
    public void cancel(@NotNull final PureException exception) {
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
