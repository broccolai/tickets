package love.broccolai.tickets.minecraft.paper.listeners;

import com.google.inject.Inject;
import love.broccolai.tickets.api.service.ProfileService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class UpdateProfileListener implements Listener {

    private final ProfileService profileService;

    @Inject
    public UpdateProfileListener(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.profileService.updateUsername(player.getUniqueId(), player.getName());
    }
}
