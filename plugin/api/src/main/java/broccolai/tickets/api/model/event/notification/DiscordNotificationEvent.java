package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.service.intergrations.DiscordService;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface DiscordNotificationEvent extends NotificationEvent {

    void discord(@NonNull DiscordService discordService);

}
