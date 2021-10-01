package broccolai.tickets.core.service.user.snapshot;

import broccolai.corn.core.Lists;
import broccolai.tickets.api.model.service.Disposable;
import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Singleton
@Order(ExecutionOrder.FIRST)
public final class CacheSnapshotService implements SoulSnapshotService, Disposable {

    private final Provider<StorageService> storageService;

    private final Map<UUID, String> uuidMappings = new HashMap<>();
    private final Map<String, UUID> nameMappings = new HashMap<>();

    @Inject
    public CacheSnapshotService(final @NonNull Provider<StorageService> storageService) {
        this.storageService = storageService;
    }

    @Override
    public @Nullable SoulSnapshot handleUniqueId(final @NonNull UUID uuid) {
        if (!this.uuidMappings.containsKey(uuid)) {
            return null;
        }

        String name = this.uuidMappings.get(uuid);
        return new SoulSnapshot(uuid, name);
    }

    @Override
    public @Nullable SoulSnapshot handleName(final @NonNull String name) {
        if (!this.nameMappings.containsKey(name)) {
            return null;
        }

        UUID uuid = this.nameMappings.get(name);
        return new SoulSnapshot(uuid, name);
    }

    public void cache(final @NonNull SoulSnapshot soulSnapshot) {
        this.nameMappings.put(soulSnapshot.username(), soulSnapshot.uuid());
        this.uuidMappings.put(soulSnapshot.uuid(), soulSnapshot.username());
    }

    @Override
    public void dispose() {
        List<SoulSnapshot> snapshots = Lists.map(this.uuidMappings.entrySet(), e -> {
            return new SoulSnapshot(e.getKey(), e.getValue());
        });

        this.storageService.get().saveSnapshots(snapshots);
    }

}
