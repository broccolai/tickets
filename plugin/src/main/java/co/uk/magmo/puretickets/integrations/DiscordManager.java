package co.uk.magmo.puretickets.integrations;

import co.uk.magmo.puretickets.configuration.Config;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.external.GsonMapper;

import java.util.HashMap;
import java.util.logging.Logger;

public class DiscordManager {
    HttpClient client = HttpClient.newBuilder()
            .withBaseURL("https://tickets.magmo.co.uk/api/v2")
            .build();

    private final Logger logger;

    private final Boolean enabled;
    private final String guild;
    private final String token;

    public DiscordManager(Logger logger, Config config) {
        this.logger = logger;
        this.enabled = config.DISCORD__ENABLED;
        this.guild = config.DISCORD__GUILD;
        this.token = config.DISCORD__TOKEN;
    }

    public void sendInformation(String color, String author, Integer id, String action, HashMap<String, String> fields) {
        if (!enabled) return;

        JsonObject json = new JsonObject();

        json.addProperty("color", color);
        json.addProperty("author", author);
        json.addProperty("id", id);
        json.addProperty("action", action);
        json.addProperty("color", color);

        if (!fields.isEmpty()) {
            JsonObject content = new JsonObject();

            fields.forEach(content::addProperty);

            json.add("fields", content);
        }

        EntityMapper entityMapper = EntityMapper.newInstance()
                .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, new Gson()));

        client.post("/announce")
                .withMapper(entityMapper)
                // todo: add actual auth header when backend is rewritten
                .withHeader(guild, token)
                .onException(Throwable::printStackTrace)
                .onStatus(200, response -> {})
                .onRemaining(response -> logger.warning(String.valueOf(response.getStatusCode())))
                .execute();
    }
}

