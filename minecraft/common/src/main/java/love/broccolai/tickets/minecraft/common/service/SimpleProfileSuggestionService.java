package love.broccolai.tickets.minecraft.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import org.jspecify.annotations.NullMarked;

@Singleton
@NullMarked
public final class SimpleProfileSuggestionService implements ProfileSuggestionService {

    private final OnlineProfileProvider onlineProfiles;
    private final ProfileService profileService;

    @Inject
    public SimpleProfileSuggestionService(
        final OnlineProfileProvider onlineProfiles,
        final ProfileService profileService
    ) {
        this.onlineProfiles = onlineProfiles;
        this.profileService = profileService;
    }

    @Override
    public Collection<String> usernames() {
        Set<String> usernames = new LinkedHashSet<>();

        usernames.addAll(this.onlineProfiles.usernames());
        this.profileService.cached().stream()
            .map(Profile::username)
            .forEach(usernames::add);

        return List.copyOf(usernames);
    }
}
