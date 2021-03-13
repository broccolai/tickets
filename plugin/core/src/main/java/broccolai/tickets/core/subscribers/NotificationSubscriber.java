package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NotificationSubscriber implements Subscriber {

    private static final Gson GSON = new GsonBuilder().create();

    private final UserService<?, ?> userService;
    private final MessageService messageService;
    private final DiscordService discordService;

    @Inject
    public NotificationSubscriber(
            final @NonNull UserService<?, ?> userService,
            final @NonNull MessageService messageService,
            final @NonNull DiscordService discordService
    ) {
        this.userService = userService;
        this.messageService = messageService;
        this.discordService = discordService;
    }

    @Subscribe
    public void onNotificationEvent(final @NonNull NotificationEvent event) {
        event.sender(this.messageService);

        TargetPair target = event.target(this.messageService);
        Soul targetSoul = this.userService.wrap(target.uuid());

        if (targetSoul instanceof OnlineSoul) {
            OnlineSoul onlineSoul = (OnlineSoul) targetSoul;
            onlineSoul.sendMessage(target.component());
        }

        event.staff(this.messageService);
        event.discord(this.discordService);
    }

}
