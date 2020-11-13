package broccolai.tickets.core.events;

import broccolai.tickets.core.exceptions.PureException;
import net.kyori.event.Cancellable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

public abstract class ExceptionEvent implements Event, Cancellable {

    private @Nullable PureException exception = null;
    private boolean isCancelled = false;

    @Override
    public final boolean cancelled() {
        return this.isCancelled;
    }

    @Override
    public final void cancelled(final boolean cancel) {
        this.isCancelled = cancel;
    }

    /**
     * Cancel the event with a PureException
     *
     * @param exception the PureException to use
     */
    public final void cancel(final @NonNull PureException exception) {
        this.exception = exception;
        this.isCancelled = true;
    }

    /**
     * Get the exception
     *
     * @return Optional pure exception
     */
    public final @NonNull Optional<PureException> getException() {
        return Optional.ofNullable(this.exception);
    }

}
