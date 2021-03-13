package broccolai.tickets.core.service.interaction;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.service.interactions.InteractionService;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CachedInteractionService implements InteractionService {

    private final Collection<Interaction> queued = new ArrayList<>();

    public void queue(final @NonNull Interaction interaction) {
        this.queued.add(interaction);
    }

    public Collection<Interaction> queued() {
        Collection<Interaction> queued = new ArrayList<>(this.queued);
        this.queued.clear();

        return queued;
    }

}
