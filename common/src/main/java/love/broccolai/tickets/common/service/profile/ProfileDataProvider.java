package love.broccolai.tickets.common.service.profile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.StorageService;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class ProfileDataProvider implements PartialProfileProvider {

    private final StorageService storageService;

    @Inject
    public ProfileDataProvider(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public Map<UUID, Profile> handleRequests(final List<UUID> requests) {
        return this.storageService.loadProfiles(requests)
            .stream()
            .collect(Collectors.toMap(Profile::uuid, profile -> profile));
    }

}
