package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.user.PlayerUser;
import net.kyori.event.AbstractCancellable;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketConstructionEvent extends AbstractCancellable implements SoulEvent {

    private final PlayerUser soul;
    private final MessageInteraction message;

    public TicketConstructionEvent(final @NonNull PlayerUser soul, final @NonNull MessageInteraction message) {
        this.soul = soul;
        this.message = message;
    }

    @Override
    public @NonNull PlayerUser soul() {
        return this.soul;
    }

    public @NonNull MessageInteraction interaction() {
        return this.message;
    }

}
