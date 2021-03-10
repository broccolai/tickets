package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.NotificationEvent;
import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NotificationSubscriber implements Subscriber {

    private static final Gson GSON = new GsonBuilder().create();

    private final MessageService messageService;
    private final DiscordService discordService;

    @Inject
    public NotificationSubscriber(final MessageService messageService, final DiscordService discordService) {
        this.messageService = messageService;
        this.discordService = discordService;
    }

    @Subscribe
    public void onNotificationEvent(final @NonNull NotificationEvent event) {
        event.sender(this.messageService);
        event.target(this.messageService);
        event.staff(this.messageService);
        event.discord(this.discordService);
    }

}
//
//    JsonObject json = new JsonObject();
//
//                        json.add("ticket", event.getInvolvedTicket().toJson());
//                                json.add("author", GSON.toJsonTree(event.getSender()));
//                                json.addProperty("action", reason.name());
