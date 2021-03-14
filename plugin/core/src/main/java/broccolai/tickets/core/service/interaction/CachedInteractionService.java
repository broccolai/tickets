package broccolai.tickets.core.service.interaction;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.interactions.InteractionService;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CachedInteractionService implements InteractionService {

    private final Multimap<Integer, Interaction> queued = MultimapBuilder.hashKeys().hashSetValues().build();

    @Override
    public final void queue(final @NonNull Ticket ticket, final @NonNull Interaction interaction) {
        this.queued.put(ticket.id(), interaction);
    }

    @Override
    public final Multimap<Integer, Interaction> queued() {
        Multimap<Integer, Interaction> queued = MultimapBuilder.hashKeys().hashSetValues().build(this.queued);
        this.queued.clear();

        return queued;
    }

}
