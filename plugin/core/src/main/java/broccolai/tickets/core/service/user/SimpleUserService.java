package broccolai.tickets.core.service.user;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.api.utilities.Either;
import broccolai.tickets.core.service.user.snapshot.AshconSnapshotService;
import broccolai.tickets.core.service.user.snapshot.CacheSnapshotService;
import cloud.commandframework.services.ServicePipeline;
import com.google.inject.Injector;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.UUID;

public abstract class SimpleUserService implements UserService {

    private static final TypeToken<SoulSnapshotService> SNAPSHOT_SERVICE = new TypeToken<>() {
    };
    private final ServicePipeline soulSnapshotPipeline = ServicePipeline.builder().build();
    private final CacheSnapshotService cacheSnapshotService;

    public SimpleUserService(final @NonNull Injector injector) {
        this.soulSnapshotPipeline.registerServiceType(SNAPSHOT_SERVICE, injector.getInstance(AshconSnapshotService.class));
        this.cacheSnapshotService = injector.getInstance(CacheSnapshotService.class);
        this.registerSnapshotService(this.cacheSnapshotService);
    }

    @Override
    public final @NonNull Soul wrap(final @NonNull UUID uuid) {

        if (uuid.equals(ConsoleSoul.UNIQUE_ID)) {
            return this.console();
        }

        if (this.isOnline(uuid)) {
            return this.player(uuid);
        }

        return this.snapshot(uuid);
    }

    @Override
    public final @NonNull Soul wrap(final @NonNull String name) {
        if (name.equalsIgnoreCase("CONSOLE")) {
            return this.console();
        }

        return this.wrap(this.snapshot(name).uuid());
    }

    @Override
    public final @NonNull SoulSnapshot snapshot(@NonNull final UUID uuid) {
        Either<UUID, String> context = Either.left(uuid);

        SoulSnapshot snapshot = this.soulSnapshotPipeline.pump(context)
            .through(SoulSnapshotService.class)
            .getResult();
        this.cacheSnapshotService.cache(snapshot);

        return snapshot;
    }

    @Override
    public final @NonNull SoulSnapshot snapshot(@NonNull final String name) {
        Either<UUID, String> context = Either.right(name);

        SoulSnapshot snapshot = this.soulSnapshotPipeline.pump(context)
                .through(SoulSnapshotService.class)
                .getResult();
        this.cacheSnapshotService.cache(snapshot);

        return snapshot;
    }

    protected abstract boolean isOnline(@NonNull UUID uuid);

    protected final void registerSnapshotService(final @NonNull SoulSnapshotService service) {
        this.soulSnapshotPipeline.registerServiceImplementation(SNAPSHOT_SERVICE, service, Collections.emptyList());
    }

}
