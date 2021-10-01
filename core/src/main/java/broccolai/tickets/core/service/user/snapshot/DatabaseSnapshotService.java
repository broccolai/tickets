package broccolai.tickets.core.service.user.snapshot;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Singleton
@Order(ExecutionOrder.LATER)
public final class DatabaseSnapshotService implements SoulSnapshotService {

    private final StorageService storageService;

    @Inject
    public DatabaseSnapshotService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public @Nullable SoulSnapshot handleUniqueId(final @NonNull UUID uuid) {
        return this.storageService.snapshot(uuid);
    }

    @Override
    public @Nullable SoulSnapshot handleName(final @NonNull String name) {
        return this.storageService.snapshot(name);
    }

}
