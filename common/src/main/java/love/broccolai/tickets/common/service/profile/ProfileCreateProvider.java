package love.broccolai.tickets.common.service.profile;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.StorageService;
import org.jspecify.annotations.NullMarked;

/**
 * Final provider to use, creates the profile.
 */
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
            String username = this.usernameProvider.username(request);
            Profile profile = new Profile(request, username);

            results.put(request, profile);
            this.storageService.insertProfile(profile);
        }

        return results;
    }

}
