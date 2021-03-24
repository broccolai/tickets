package broccolai.tickets.api.model.event.impl;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.user.PlayerSoul;
import net.kyori.event.AbstractCancellable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class TicketConstructionEvent extends AbstractCancellable implements SoulEvent {

    private final PlayerSoul soul;
    private final MessageInteraction message;

    public TicketConstructionEvent(final @NonNull PlayerSoul soul, final @NonNull MessageInteraction message) {
        this.soul = soul;
        this.message = message;
    }

    @Override
    public @NonNull PlayerSoul soul() {
        return this.soul;
    }

    public @NonNull MessageInteraction interaction() {
        return this.message;
    }

}
