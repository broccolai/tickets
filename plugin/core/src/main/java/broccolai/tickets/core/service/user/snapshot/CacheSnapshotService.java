package broccolai.tickets.core.service.user.snapshot;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
@Order(ExecutionOrder.FIRST)
public final class CacheSnapshotService implements SoulSnapshotService {

    private final Map<UUID, String> uuidMappings = new HashMap<>();
    private final Map<String, UUID> nameMappings = new HashMap<>();

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

}
