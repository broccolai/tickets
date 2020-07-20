package co.uk.magmo.puretickets.integrations;

import co.uk.magmo.puretickets.configuration.Config;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.external.GsonMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordManager {
    private final HttpClient client;

    private final Logger logger;
    private final Boolean enabled;

    public DiscordManager(Logger logger, Config config) {
        this.client = HttpClient.newBuilder()
                .withBaseURL("http://tickets.broccol.ai/api/v1")
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

    public void sendInformation(String color, String author, UUID uuid, Integer id, String action, HashMap<String, String> fields) {
        if (!enabled) return;

        JsonObject json = new JsonObject();

        json.addProperty("color", color);
        json.addProperty("author", author);
        json.addProperty("id", id);
        json.addProperty("uuid", uuid.toString());
        json.addProperty("action", action);
        json.addProperty("color", color);

        if (!fields.isEmpty()) {
            JsonObject content = new JsonObject();

            fields.forEach(content::addProperty);

            json.add("fields", content);
        }

        EntityMapper entityMapper = EntityMapper.newInstance()
                .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, new GsonBuilder().create()));
        client.post("/announce")
                .withMapper(entityMapper)
                .withInput(() -> json)
                .onException(Throwable::printStackTrace)
                .onStatus(200, response -> {})
                .onRemaining(response -> logger.warning(String.valueOf(response.getStatusCode())))
                .execute();
    }
}

