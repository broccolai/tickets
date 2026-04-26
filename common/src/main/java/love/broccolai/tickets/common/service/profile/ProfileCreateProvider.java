package love.broccolai.tickets.common.service.profile;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.StorageService;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileCreateProvider implements PartialProfileProvider {

    private final StorageService storageService;
    private final UUIDUsernameConverter usernameProvider;

    @Inject
    public ProfileCreateProvider(
        final StorageService storageService,
        final UUIDUsernameConverter usernameProvider
    ) {
        this.storageService = storageService;
        this.usernameProvider = usernameProvider;
    }

    @Override
    public Map<UUID, Profile> handleRequests(final List<UUID> requests) {
        Map<UUID, Profile> results = new HashMap<>();

        for (UUID request : requests) {
            Profile profile = this.usernameProvider.username(request)
                .map(username -> new Profile(request, username))
                .orElse(null);

            if (profile != null) {
                results.put(request, profile);
                this.storageService.insertProfile(profile);
            }
        }

        return results;
    }
}
