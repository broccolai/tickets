package love.broccolai.tickets.common.service.profile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.api.model.profile.Profile;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ProfileCacheProvider implements PartialProfileProvider {

    private final Cache<UUID, Profile> uuidCache;
    private final Cache<String, Profile> usernameCache;

    @Inject
    public ProfileCacheProvider() {
        this.uuidCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build();
        this.usernameCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build();
    }

    @Override
    public Map<UUID, Profile> handleRequests(final List<UUID> requests) {
        Map<UUID, Profile> results = new HashMap<>();

        for (UUID request : requests) {
            this.find(request)
                .ifPresent(profile -> results.put(request, profile));
        }

        return results;
    }

    public void cache(final Map<UUID, Profile> entries) {
        entries.values().forEach(this::cache);
    }

    public void cache(final Profile profile) {
        Profile existing = this.uuidCache.getIfPresent(profile.uuid());

        if (existing != null && !existing.username().equalsIgnoreCase(profile.username())) {
            this.usernameCache.invalidate(this.key(existing.username()));
        }

        this.uuidCache.put(profile.uuid(), profile);
        this.usernameCache.put(this.key(profile.username()), profile);
    }

    public Optional<Profile> find(final UUID uniqueId) {
        return Optional.ofNullable(this.uuidCache.getIfPresent(uniqueId));
    }

    public Optional<Profile> find(final String username) {
        return Optional.ofNullable(this.usernameCache.getIfPresent(this.key(username)));
    }

    public Collection<Profile> cached() {
        return List.copyOf(this.uuidCache.asMap().values());
    }

    private String key(final String username) {
        return username.toLowerCase(Locale.ROOT);
    }
}
