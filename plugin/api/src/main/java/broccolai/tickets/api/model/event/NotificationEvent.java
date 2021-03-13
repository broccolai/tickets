package broccolai.tickets.api.model.event;

import broccolai.tickets.api.model.message.TargetPair;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface NotificationEvent extends Event {

    default void sender(@NonNull MessageService messageService) {

    }

    default @Nullable TargetPair target(@NonNull MessageService messageService) {
        return null;
    }

    default void staff(@NonNull MessageService messageService) {

    }

    default void discord(@NonNull DiscordService discordService) {

    }

}
