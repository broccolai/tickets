package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.service.profile.PartialProfileProvider;
import love.broccolai.tickets.common.service.profile.ProfileCacheProvider;
import love.broccolai.tickets.common.service.profile.ProfileCreateProvider;
import love.broccolai.tickets.common.service.profile.ProfileDataProvider;
import love.broccolai.tickets.common.service.profile.ProfileServiceContext;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import org.incendo.cloud.services.ServicePipeline;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public class PipelineProfileService implements ProfileService {

    private final ServicePipeline pipeline = ServicePipeline.builder().build();

    private final StorageService storageService;
    private final ProfileCacheProvider cacheProvider;
    private final UUIDUsernameConverter uuidUsernameConverter;

    @Inject
    public PipelineProfileService(
        final StorageService storageService,
        final ProfileCreateProvider createProvider,
        final ProfileDataProvider dataProvider,
        final ProfileCacheProvider cacheProvider,
        final UUIDUsernameConverter uuidUsernameConverter
    ) {
        this.storageService = storageService;
        this.cacheProvider = cacheProvider;
        this.uuidUsernameConverter = uuidUsernameConverter;

        this.pipeline
            .registerServiceType(PartialProfileProvider.TYPE, createProvider)
            .registerServiceImplementation(
                PartialProfileProvider.TYPE,
                dataProvider,
                Collections.emptyList()
            )
            .registerServiceImplementation(
                PartialProfileProvider.TYPE,
                cacheProvider,
                Collections.emptyList()
            );
    }

    @Override
    public Optional<Profile> get(final UUID uniqueId) {
        Profile profile = this.get(Collections.singletonList(uniqueId)).get(uniqueId);

        return Optional.ofNullable(profile);
    }

    @Override
    public Optional<Profile> get(String username) {
        UUID uuid = this.uuidUsernameConverter.uuid(username);

        return this.get(uuid);
    }

    @Override
    public final Map<UUID, Profile> get(final Collection<UUID> uniqueIds) {
        Map<UUID, Profile> results = this.pipeline.pump(new ProfileServiceContext(uniqueIds))
            .through(PartialProfileProvider.TYPE)
            .complete();

        this.cacheProvider.cache(results);

        return results;
    }

    @Override
    public Optional<Profile> modify(UUID uuid, Function<Profile, Boolean> modifier) {
        return this.get(uuid).map(profile -> {
            if (modifier.apply(profile)) {
                this.storageService.updateProfile(profile);
            }

            return profile;
        });
    }

    @Override
    public Collection<Profile> find() {
        Set<UUID> relevantUniqueIds = this.storageService.findTickets(EnumSet.of(TicketStatus.OPEN, TicketStatus.PICKED), null, null)
            .stream()
            .map(Ticket::creator)
            .collect(Collectors.toSet());

        return this.get(relevantUniqueIds).values();
    }

}
