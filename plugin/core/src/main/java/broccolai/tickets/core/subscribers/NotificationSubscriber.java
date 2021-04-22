package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.SoulEvent;
import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.notification.DiscordNotificationEvent;
import broccolai.tickets.api.model.event.notification.SenderNotificationEvent;
import broccolai.tickets.api.model.event.notification.StaffNotificationEvent;
import broccolai.tickets.api.model.event.notification.TargetNotificationEvent;
import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.UUID;

public final class NotificationSubscriber implements Subscriber {

    private final UserService userService;
    private final MessageService messageService;
    private final DiscordService discordService;
    private final StorageService storageService;

    @Inject
    public NotificationSubscriber(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService,
            final @NonNull DiscordService discordService,
            final @NonNull StorageService storageService
    ) {
        this.userService = userService;
        this.messageService = messageService;
        this.discordService = discordService;
        this.storageService = storageService;
    }

    @Override
    public void register(final @NonNull EventService eventService) {
        eventService.register(SenderNotificationEvent.class, this::onSenderNotification);
        eventService.register(TargetNotificationEvent.class, this::onTargetNotification);
        eventService.register(StaffNotificationEvent.class, this::onStaffNotification);
        eventService.register(DiscordNotificationEvent.class, this::onDiscordNotification);
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
            return;
        }

        this.storageService.saveNotification(targetSoul, target.component());
    }

    public void onStaffNotification(final @NonNull StaffNotificationEvent event) {
        Component message = event.staff(this.messageService);

        final UUID ignore;

        if (event instanceof SoulEvent) {
            SoulEvent soulEvent = (SoulEvent) event;
            ignore = soulEvent.soul().uuid();
        } else {
            ignore = null;
        }

        this.userService.players().forEach(soul -> {
            if (soul.permission("tickets.staff.announce") && !soul.uuid().equals(ignore)) {
                soul.sendMessage(message);
            }
        });

        this.userService.console().sendMessage(message);
    }

    public void onDiscordNotification(final @NonNull DiscordNotificationEvent event) {
        this.discordService.announce(event.discord(this.userService));
    }
}
