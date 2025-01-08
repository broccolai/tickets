package love.broccolai.tickets.common.service.profile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ProfileCacheProvider implements PartialProfileProvider {

    private final Cache<UUID, Profile> uuidCache;

    @Inject
    public ProfileCacheProvider() {
        this.uuidCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build();
    }

    @Override
    public Map<UUID, Profile> handleRequests(final List<UUID> requests) {
        Map<UUID, Profile> results = new HashMap<>();

        for (final UUID request : requests) {
            Profile profile = this.uuidCache.getIfPresent(request);

            if (profile != null) {
                results.put(request, profile);
            }
        }

        return results;
    }

    public void cache(final Map<UUID, Profile> entries) {
        this.uuidCache.putAll(entries);
    }
}
