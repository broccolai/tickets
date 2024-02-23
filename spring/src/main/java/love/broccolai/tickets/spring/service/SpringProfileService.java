package love.broccolai.tickets.spring.service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;

//todo: temporary until profile pipeline is setup
public final class SpringProfileService implements ProfileService {
    @Override
    public Profile get(UUID uuid) {
        return new Profile(uuid);
    }

    @Override
    public Profile get(String username) {
        throw new IllegalCallerException();
    }

    @Override
    public Map<UUID, Profile> get(Collection<UUID> uuids) {
        return Trove.of(uuids)
            .collect(Collectors.toMap(Function.identity(), Profile::new));
    }

    @Override
    public Collection<String> onlineUsernames() {
        throw new IllegalCallerException();
    }
}
