package love.broccolai.tickets.api.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import love.broccolai.tickets.api.model.profile.Profile;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ProfileService {

    Optional<Profile> find(UUID uuid);

    Optional<Profile> find(String username);

    Map<UUID, Profile> load(Collection<UUID> uuids);

    Optional<Profile> update(Profile profile);

    Optional<Profile> updateUsername(UUID uuid, String username);

    Collection<Profile> active();

    Collection<Profile> cached();

    default Optional<Profile> get(final UUID uuid) {
        return this.find(uuid);
    }

    default Optional<Profile> get(final String username) {
        return this.find(username);
    }

    default Map<UUID, Profile> get(final Collection<UUID> uuids) {
        return this.load(uuids);
    }

    default Optional<Profile> modify(final UUID uuid, final UnaryOperator<Profile> modifier) {
        return this.find(uuid).flatMap(profile -> {
            Profile updatedProfile = modifier.apply(profile);

            if (updatedProfile.equals(profile)) {
                return Optional.of(profile);
            }

            return this.update(updatedProfile);
        });
    }

    default Collection<Profile> find() {
        return this.active();
    }
}
