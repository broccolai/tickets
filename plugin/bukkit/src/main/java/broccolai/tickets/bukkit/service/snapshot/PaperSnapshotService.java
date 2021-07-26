package broccolai.tickets.bukkit.service.snapshot;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.inject.Singleton;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Singleton
@Order(ExecutionOrder.SOON)
public final class PaperSnapshotService implements SoulSnapshotService {

    @Override
    public @Nullable SoulSnapshot handleUniqueId(final @NonNull UUID uuid) {
        PlayerProfile profile = Bukkit.createProfile(uuid);
        if (profile.completeFromCache()) {
            return new SoulSnapshot(profile.getId(), profile.getName());
        }

        return null;
    }

    @Override
    public @Nullable SoulSnapshot handleName(final @NonNull String name) {
        PlayerProfile profile = Bukkit.createProfile(name);
        if (profile.completeFromCache()) {
            return new SoulSnapshot(profile.getId(), profile.getName());
        }

        return null;
    }

}
