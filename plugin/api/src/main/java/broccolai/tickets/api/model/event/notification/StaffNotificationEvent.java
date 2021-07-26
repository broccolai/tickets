package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface StaffNotificationEvent extends NotificationEvent {

    @NonNull Component staff(@NonNull OldMessageService oldMessageService);

}
