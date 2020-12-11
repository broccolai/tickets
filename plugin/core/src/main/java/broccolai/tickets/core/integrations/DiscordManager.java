package broccolai.tickets.core.integrations;

import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.EventListener;
import broccolai.tickets.core.events.api.NotificationEvent;
import broccolai.tickets.core.interactions.NotificationReason;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.external.GsonMapper;
import net.kyori.event.method.annotation.Subscribe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Base64;
import java.util.logging.Logger;

public class DiscordManager implements EventListener {

    private final HttpClient client;
    private final Logger logger;
    private final String serverName;
    private final Boolean enabled;

    /**
     * Initialise the Discord Manager
     *
     * @param logger the logger to log errors to
     * @param config the config instance to use
     */
    public DiscordManager(final @NonNull Logger logger, final @NonNull Config config) {
        EntityMapper entityMapper = EntityMapper.newInstance()
                .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, new GsonBuilder().create()));

        this.client = HttpClient.newBuilder()
                .withBaseURL(config.getApiDomain() + "/api/v2")
                .withDecorator((req) -> {
                    String raw = config.getDiscordGuild() + ":" + config.getDiscordToken();
                    byte[] encoded = Base64.getEncoder().encode(raw.getBytes());
                    String header = "Basic " + new String(encoded);

                    req.withHeader("Authorization", header);
                })
                .withEntityMapper(entityMapper)
                .build();

        this.logger = logger;
        this.enabled = config.getDiscordEnabled();

        if (config.getDiscordName().length() > 0) {
            this.serverName = config.getDiscordName();
        } else {
            this.serverName = null;
        }
    }

    /**
     * @param event Notification event
     */
    @Subscribe
    public void onNotification(final @NonNull NotificationEvent event) {
        NotificationReason reason = event.getReason();

        if (!enabled || !reason.discord()) {
            return;
        }

        JsonObject json = new JsonObject();

        json.addProperty("server", this.serverName);
        json.add("ticket", event.getInvolvedTicket().toJson());
        json.add("author", event.getSender().toJson());
        json.addProperty("action", reason.name());

        client.post("/announce")
                .withInput(() -> json)
                .onException(Throwable::printStackTrace)
                .onStatus(200, response -> {
                })
                .onRemaining(response -> logger.warning(String.valueOf(response.getStatusCode())))
                .execute();
    }

}

