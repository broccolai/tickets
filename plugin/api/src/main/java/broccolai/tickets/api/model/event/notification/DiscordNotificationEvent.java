package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.service.user.UserService;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface DiscordNotificationEvent extends NotificationEvent {

    @NonNull JsonObject discord(@NonNull UserService userService);

}
