package love.broccolai.tickets.api.service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import love.broccolai.tickets.api.model.proflie.Profile;

public interface ProfileService {

    Profile get(UUID uuid);

    Profile get(String username);

    Map<UUID, Profile> get(Collection<UUID> uuids);

    Collection<String> onlineUsernames();

}
