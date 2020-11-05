package broccolai.tickets.events;

import broccolai.tickets.exceptions.PureException;
import org.bukkit.event.Cancellable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

class ThrowableEvent extends BaseEvent implements Cancellable {

    private @Nullable PureException exception = null;
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
    public void cancel(final @NonNull PureException exception) {
        this.exception = exception;
        isCancelled = true;
    }

    public boolean hasException() {
        return exception != null;
    }

    public @NonNull PureException getException() {
        assert exception != null;
        return exception;
    }

}
