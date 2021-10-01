package broccolai.tickets.core.tasks;

import broccolai.tickets.api.model.task.Task;
import broccolai.tickets.api.service.storage.StorageService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SaveTask implements Task {

    private final StorageService storageService;

    @Inject
    public SaveTask(
            final @NonNull StorageService storageService
    ) {
        this.storageService = storageService;
    }

    @Override
    public void run() {
        this.storageService.clear();
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
