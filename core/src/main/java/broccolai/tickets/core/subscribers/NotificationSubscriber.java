package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.notification.DiscordNotificationEvent;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NotificationSubscriber implements Subscriber {

    private final UserService userService;
    private final DiscordService discordService;

    @Inject
    public NotificationSubscriber(
            final @NonNull UserService userService,
            final @NonNull DiscordService discordService
    ) {
        this.userService = userService;
        this.discordService = discordService;
    }

    @Override
    public void register(final @NonNull EventService eventService) {
        eventService.register(DiscordNotificationEvent.class, this::onDiscordNotification);
    }

    public void onDiscordNotification(final @NonNull DiscordNotificationEvent event) {
        this.discordService.announce(event.discord(this.userService));
    }

}
