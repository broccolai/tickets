package broccolai.tickets.core.tasks;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.storage.StorageService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

//todo: run at server stop
public final class SaveTask implements Task {

    private final StorageService storageService;
    private final InteractionService interactionService;

    @Inject
    public SaveTask(
            final @NonNull StorageService storageService,
            final @NonNull InteractionService interactionService
    ) {
        this.storageService = storageService;
        this.interactionService = interactionService;
    }

    @Override
    public void run() {
        Collection<Interaction> interactions = this.interactionService.queued();

        if (interactions.isEmpty()) {
            return;
        }

        this.storageService.saveInteractions(interactions);
    }

    @Override
    public long delay() {
        return 3600;
    }

    @Override
    public long repeat() {
        return 3600;
    }

}
