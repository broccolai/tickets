package love.broccolai.tickets.minecraft.paper;

import java.util.Collection;
import love.broccolai.tickets.minecraft.common.service.OnlineProfileProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperOnlineProfileProvider implements OnlineProfileProvider {

    @Override
    public Collection<String> usernames() {
        return Bukkit.getOnlinePlayers()
            .stream()
            .map(Player::getName)
            .toList();
    }
}
