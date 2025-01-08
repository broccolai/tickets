package love.broccolai.tickets.api.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import love.broccolai.tickets.api.model.proflie.Profile;

public interface ProfileService {

    Optional<Profile> get(UUID uuid);

    Optional<Profile> get(String username);

    Map<UUID, Profile> get(Collection<UUID> uuids);

    Optional<Profile> modify(UUID uuid, Function<Profile, Boolean> modifier);

    Collection<Profile> find();

}
