package broccolai.tickets.velocity.subscribers;

import broccolai.tickets.api.model.event.impl.SoulJoinEvent;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class PlayerJoinSubscriber implements VelocitySubscriber {

    private final EventService eventService;
    private final TaskService taskService;
    private final UserService userService;

    @Inject
    public PlayerJoinSubscriber(
            final @NonNull EventService eventService,
            final @NonNull TaskService taskService,
            final @NonNull UserService userService
    ) {
        this.eventService = eventService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Subscribe
    public void onPlayerConnect(final @NonNull ServerConnectedEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        this.taskService.async(() -> {
            PlayerSoul soul = this.userService.player(uuid);
            SoulJoinEvent soulEvent = new SoulJoinEvent(soul);

            this.eventService.post(soulEvent);
        });
    }

}
