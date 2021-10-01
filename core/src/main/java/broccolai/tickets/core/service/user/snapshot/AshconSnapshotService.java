package broccolai.tickets.core.service.user.snapshot;

import broccolai.tickets.api.model.user.SoulSnapshot;
import broccolai.tickets.core.service.user.SoulSnapshotService;
import cloud.commandframework.services.ExecutionOrder;
import cloud.commandframework.services.annotations.Order;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import com.intellectualsites.http.HttpClient;
import com.intellectualsites.http.HttpResponse;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

@Singleton
@Order(ExecutionOrder.LAST)
public final class AshconSnapshotService implements SoulSnapshotService {

    private static final Pattern UUID_REGEX =
            Pattern.compile("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)");

    private static final String AGENT = "minecraft/";
    private static final String NAMES_PATH = "/names";

    private final HttpClient client = HttpClient.newBuilder()
            .withBaseURL("https://api.mojang.com/user/profiles/")
            .build();

    @Override
    public @NotNull SoulSnapshot handleUniqueId(final @NonNull UUID uuid) {
        HttpResponse response = this.client.get(this.mojangIdFromUniqueId(uuid) + NAMES_PATH).execute();
        JsonArray jsonArray = response.getResponseEntity(JsonObject.class).getAsJsonArray();
        JsonObject json = (JsonObject) jsonArray.get(0);
        String name = json.get("name").getAsString();

        return new SoulSnapshot(uuid, name);
    }

    @Override
    public @NotNull SoulSnapshot handleName(final @NonNull String name) {
        HttpResponse response = this.client.get(AGENT + name).execute();
        JsonObject json = response.getResponseEntity(JsonObject.class);
        String id = json.get("id").getAsString();
        UUID uuid = this.uniqueIdFromMojangId(id);

        return new SoulSnapshot(uuid, name);
    }

    private @NonNull UUID uniqueIdFromMojangId(final @NonNull String id) {
        Matcher matcher = UUID_REGEX.matcher(id);
        return UUID.fromString(matcher.replaceAll("$1-$2-$3-$4-$5"));
    }

    private @NonNull String mojangIdFromUniqueId(final @NonNull UUID uuid) {
        return uuid.toString().replaceAll("-", "");
    }

}
