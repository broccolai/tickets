package broccolai.tickets.integrations;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.utilities.generic.UserUtilities;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.external.GsonMapper;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordManager {
    private final HttpClient client;

    private final Logger logger;
    private final Boolean enabled;

    public DiscordManager(Logger logger, Config config) {
        this.client = HttpClient.newBuilder()
            .withBaseURL(config.API__DOMAIN + "/api/v2")
            .withDecorator((req) -> {
                String raw = config.DISCORD__GUILD + ":" + config.DISCORD__TOKEN;
                byte[] encoded = Base64.getEncoder().encode(raw.getBytes());
                String header = "Basic " + new String(encoded);

                req.withHeader("Authorization", header);
            })
            .build();

        this.logger = logger;
        this.enabled = config.DISCORD__ENABLED;
    }

    public void sendInformation(Ticket ticket, UUID author, String action) {
        if (!enabled) {
            return;
        }

        JsonObject json = new JsonObject();
        JsonObject authorJson = new JsonObject();

        if (author == null) {
            authorJson.addProperty("name", "Console");
            authorJson.addProperty("uuid", "f78a4d8d-d51b-4b39-98a3-230f2de0c670");
        } else {
            authorJson.addProperty("name", UserUtilities.nameFromUUID(author));
            authorJson.addProperty("uuid", author.toString());
        }

        json.add("ticket", ticket.toJson());
        json.add("author", authorJson);
        json.addProperty("action", action);

        EntityMapper entityMapper = EntityMapper.newInstance()
            .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, new GsonBuilder().create()));
        client.post("/announce")
            .withMapper(entityMapper)
            .withInput(() -> json)
            .onException(Throwable::printStackTrace)
            .onStatus(200, response -> {
            })
            .onRemaining(response -> logger.warning(String.valueOf(response.getStatusCode())))
            .execute();
    }
}

