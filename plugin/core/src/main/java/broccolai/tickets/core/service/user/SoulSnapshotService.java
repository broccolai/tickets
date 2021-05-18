package broccolai.tickets.core.service.user;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.api.utilities.Either;
import cloud.commandframework.services.types.Service;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.UUID;

public interface SoulSnapshotService extends Service<Either<UUID, String>, SoulSnapshot> {

    @Override
    default @Nullable SoulSnapshot handle(@NonNull Either<UUID, String> context) {
        return context.map(this::handleUniqueId, this::handleName);
    }

    @Nullable SoulSnapshot handleUniqueId(@NonNull UUID uuid);

    @Nullable SoulSnapshot handleName(@NonNull String name);

}
