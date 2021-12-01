package broccolai.tickets.core.service.user;

import broccolai.tickets.api.model.user.UserSnapshot;
import cloud.commandframework.services.types.Service;
import java.util.UUID;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface SoulSnapshotService extends Service<Either<UUID, String>, UserSnapshot> {

    @Override
    default @Nullable UserSnapshot handle(@NonNull Either<UUID, String> context) {
        return context.right()
                .map(this::handleName)
                .or(() -> context.left().map(this::handleUniqueId))
                .orElseThrow();
    }

    @Nullable UserSnapshot handleUniqueId(@NonNull UUID uuid);

    @Nullable UserSnapshot handleName(@NonNull String name);

}
