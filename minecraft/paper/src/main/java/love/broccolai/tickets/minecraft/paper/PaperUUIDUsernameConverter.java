package love.broccolai.tickets.minecraft.paper;

import java.util.UUID;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import org.bukkit.Bukkit;

public final class PaperUUIDUsernameConverter implements UUIDUsernameConverter {

    @Override
    public String username(final UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Override
    public UUID uuid(final String username) {
        return Bukkit.getOfflinePlayerIfCached(username).getUniqueId();
    }
}
