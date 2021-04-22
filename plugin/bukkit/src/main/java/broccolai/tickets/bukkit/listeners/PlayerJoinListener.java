package broccolai.tickets.bukkit.listeners;

import broccolai.tickets.api.model.event.impl.SoulJoinEvent;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PlayerJoinListener implements Listener {

    private final EventService eventService;
    private final TaskService taskService;
    private final UserService userService;

    @Inject
    public PlayerJoinListener(
            final @NonNull EventService eventService,
            final @NonNull TaskService taskService,
            final @NonNull UserService userService
    ) {
        this.eventService = eventService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        this.taskService.async(() -> {
            PlayerSoul soul = this.userService.player(uuid);
            SoulJoinEvent soulEvent = new SoulJoinEvent(soul);

            this.eventService.post(soulEvent);
        });
    }

}
