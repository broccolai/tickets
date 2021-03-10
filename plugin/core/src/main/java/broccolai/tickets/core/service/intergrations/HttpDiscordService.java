package broccolai.tickets.core.service.intergrations;

import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.core.configuration.DiscordConfiguration;
import broccolai.tickets.core.configuration.MainConfiguration;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellectualsites.http.EntityMapper;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.external.GsonMapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Base64;
import java.util.logging.Logger;

@Singleton
public final class HttpDiscordService implements DiscordService {

    private final HttpClient client;
    private final Logger logger;
    private final String serverName;
    private final Boolean enabled;

    @Inject
    public HttpDiscordService(final @NonNull Logger logger, final @NonNull MainConfiguration mainConfiguration) {
        DiscordConfiguration config = mainConfiguration.discordConfiguration;

        EntityMapper entityMapper = EntityMapper.newInstance()
                .registerSerializer(JsonObject.class, GsonMapper.serializer(JsonObject.class, new GsonBuilder().create()));

        this.client = HttpClient.newBuilder()
                .withBaseURL(mainConfiguration.advancedConfiguration.api + "/api/v2")
                .withDecorator((req) -> {
                    String raw = config.guild + ":" + config.token;
                    byte[] encoded = Base64.getEncoder().encode(raw.getBytes());
                    String header = "Basic " + new String(encoded);

                    req.withHeader("Authorization", header);
                })
                .withEntityMapper(entityMapper)
                .build();

        this.logger = logger;
        this.enabled = config.enabled;
        this.serverName = config.name;
    }

    @Override
    public void announce(final @NonNull JsonObject json) {
        if (!this.enabled) {
            return;
        }

        json.addProperty("server", this.serverName);

        this.client.post("/announce")
                .withInput(() -> json)
                .onException(Throwable::printStackTrace)
                .onStatus(200, response -> {
                })
                .onRemaining(response -> this.logger.warning(String.valueOf(response.getStatusCode())))
                .execute();
    }

}
