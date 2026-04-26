package love.broccolai.tickets.minecraft.paper;

import java.util.Optional;
import java.util.UUID;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperUUIDUsernameConverter implements UUIDUsernameConverter {

    @Override
    public Optional<String> username(final UUID uuid) {
        return Optional.ofNullable(Bukkit.getOfflinePlayer(uuid).getName())
            .filter(username -> !username.isBlank());
    }

    @Override
    public Optional<UUID> uuid(final String username) {
        return Optional.ofNullable(Bukkit.getOfflinePlayerIfCached(username))
            .map(OfflinePlayer::getUniqueId);
    }
}
