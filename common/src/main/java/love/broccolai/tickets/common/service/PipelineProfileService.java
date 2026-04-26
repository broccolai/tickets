package love.broccolai.tickets.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.api.service.TicketSearch;
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
public final class PipelineProfileService implements ProfileService {

    private final ServicePipeline pipeline = ServicePipeline.builder().build();

    private final StorageService storageService;
    private final ProfileCacheProvider cacheProvider;
    private final UUIDUsernameConverter usernameConverter;

    @Inject
    public PipelineProfileService(
        final StorageService storageService,
        final ProfileCreateProvider createProvider,
        final ProfileDataProvider dataProvider,
        final ProfileCacheProvider cacheProvider,
        final UUIDUsernameConverter usernameConverter
    ) {
        this.storageService = storageService;
        this.cacheProvider = cacheProvider;
        this.usernameConverter = usernameConverter;

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
    public Optional<Profile> find(final UUID uniqueId) {
        Profile profile = this.load(List.of(uniqueId)).get(uniqueId);

        return Optional.ofNullable(profile);
    }

    @Override
    public Optional<Profile> find(final String username) {
        return this.cacheProvider.find(username)
            .or(() -> this.storageService.findProfile(username)
                .map(this::cache))
            .or(() -> this.usernameConverter.uuid(username)
                .flatMap(this::find));
    }

    @Override
    public Map<UUID, Profile> load(final Collection<UUID> uniqueIds) {
        if (uniqueIds.isEmpty()) {
            return Map.of();
        }

        Map<UUID, Profile> results;

        try {
            results = this.pipeline.pump(new ProfileServiceContext(uniqueIds))
                .through(PartialProfileProvider.TYPE)
                .complete();
        } catch (IllegalStateException e) {
            return Map.of();
        }

        this.cacheProvider.cache(results);

        return results;
    }

    @Override
    public Optional<Profile> update(final Profile profile) {
        this.storageService.updateProfile(profile);

        return Optional.of(this.cache(profile));
    }

    @Override
    public Optional<Profile> updateUsername(final UUID uuid, final String username) {
        return this.find(uuid)
            .flatMap(profile -> this.update(profile.withUsername(username)));
    }

    @Override
    public Collection<Profile> active() {
        Set<UUID> relevantUniqueIds = this.storageService.findTickets(
            TicketSearch.allActive()
        )
            .stream()
            .map(Ticket::creator)
            .collect(Collectors.toSet());

        return this.load(relevantUniqueIds).values();
    }

    @Override
    public Collection<Profile> cached() {
        return this.cacheProvider.cached();
    }

    private Profile cache(final Profile profile) {
        this.cacheProvider.cache(profile);

        return profile;
    }
}
