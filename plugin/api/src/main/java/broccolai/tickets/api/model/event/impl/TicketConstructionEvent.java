package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.user.PlayerSoul;
import net.kyori.event.Cancellable;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketConstructionEvent extends Cancellable.Impl implements SoulEvent {

    private final PlayerSoul soul;
    private final MessageInteraction message;

    /**
     * Initialise the construction event
     */
    public TicketConstructionEvent(final @NonNull PlayerSoul soul, final @NonNull MessageInteraction message) {
        this.soul = soul;
        this.message = message;
    }

    @Override
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

    /**
     * Get the constructed tickets message
     */
    public @NonNull MessageInteraction interaction() {
        return this.message;
    }

}
