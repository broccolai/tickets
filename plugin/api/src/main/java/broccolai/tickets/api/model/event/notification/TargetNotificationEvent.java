package broccolai.tickets.api.model.event.notification;

import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TargetNotificationEvent extends NotificationEvent {

    TargetPair target(@NonNull MessageService messageService);

}
