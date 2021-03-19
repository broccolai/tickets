package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface SenderNotificationEvent extends NotificationEvent {

    void sender(@NonNull MessageService messageService);

}
