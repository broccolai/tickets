package love.broccolai.tickets.minecraft.paper.service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

//todo: temporary until profile pipeline is setup
public final class PaperProfileService implements ProfileService {
    @Override
    public Profile get(UUID uuid) {
        return new Profile(uuid);
    }

    @Override
    public Profile get(String username) {
        UUID uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
        return this.get(uuid);
    }

    @Override
    public Map<UUID, Profile> get(Collection<UUID> uuids) {
        return Trove.of(uuids)
            .collect(Collectors.toMap(Function.identity(), Profile::new));
    }

    @Override
    public Collection<String> onlineUsernames() {
        return Trove.of(Bukkit.getOnlinePlayers())
            .map(Player::getName)
            .toList();
    }
}
