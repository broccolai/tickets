package love.broccolai.tickets.minecraft.paper;

import java.util.UUID;
import love.broccolai.tickets.minecraft.common.service.PermissionAudienceProvider;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PaperPermissionAudienceProvider implements PermissionAudienceProvider {

    @Override
    public Audience audience(final String permission, final @Nullable UUID excluded) {
        return Audience.audience(
            Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .filter(player -> excluded == null || !player.getUniqueId().equals(excluded))
                .map(player -> (Audience) player)
                .toList()
        );
    }
}
