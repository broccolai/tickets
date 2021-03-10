package broccolai.tickets.api.model.event;

import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface NotificationEvent extends Event {

    void sender(@NonNull MessageService messageService);

    void target(@NonNull MessageService messageService);

    void staff(@NonNull MessageService messageService);

    void discord(@NonNull DiscordService discordService);

}
