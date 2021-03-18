package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.notification.SenderNotificationEvent;
import broccolai.tickets.api.model.event.notification.StaffNotificationEvent;
import broccolai.tickets.api.model.event.notification.TargetNotificationEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.UUID;

public final class NotificationSubscriber implements Subscriber {

    private static final Gson GSON = new GsonBuilder().create();

    private final UserService userService;
    private final MessageService messageService;
    private final DiscordService discordService;

    @Inject
    public NotificationSubscriber(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService,
            final @NonNull DiscordService discordService
    ) {
        this.userService = userService;
        this.messageService = messageService;
        this.discordService = discordService;
    }

    @Override
    public void register(final @NonNull EventService eventService) {
        eventService.register(SenderNotificationEvent.class, this::onSenderNotification);
        eventService.register(TargetNotificationEvent.class, this::onTargetNotification);
        eventService.register(StaffNotificationEvent.class, this::onStaffNotification);
    }

    public void onSenderNotification(final @NonNull SenderNotificationEvent event) {
        event.sender(this.messageService);
    }

    public void onTargetNotification(final @NonNull TargetNotificationEvent event) {
        TargetPair target = event.target(this.messageService);

        if (target == null) {
            return;
        }

        Soul targetSoul = this.userService.wrap(target.uuid());

        if (targetSoul instanceof OnlineSoul) {
            OnlineSoul onlineSoul = (OnlineSoul) targetSoul;
            onlineSoul.sendMessage(target.component());
        }
    }

    public void onStaffNotification(final @NonNull StaffNotificationEvent event) {
        Component message = event.staff(this.messageService);

        if (message == null) {
            return;
        }

        final UUID ignore;

        if (event instanceof SoulEvent) {
            SoulEvent soulEvent = (SoulEvent) event;
            ignore = soulEvent.soul().uuid();
        } else {
            ignore = null;
        }

        //todo: send to console too
        this.userService.players().forEach(soul -> {
            if (soul.permission("tickets.staff.announce") && !soul.uuid().equals(ignore)) {
                soul.sendMessage(message);
            }
        });
    }
}
