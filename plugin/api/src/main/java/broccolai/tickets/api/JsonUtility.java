package broccolai.tickets.api;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class JsonUtility {

    private JsonUtility() {
    }

    public static JsonObject ticket(final @NonNull UserService userService, final @NonNull Ticket ticket) {
        JsonObject json = new JsonObject();
        json.addProperty("id", ticket.id());

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("name", userService.snapshot(ticket.player()).username());
        playerJson.addProperty("uuid", ticket.player().toString());
        json.add("player", playerJson);

        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("world", ticket.position().world());
        locationJson.addProperty("x", ticket.position().x());
        locationJson.addProperty("y", ticket.position().y());
        locationJson.addProperty("z", ticket.position().z());
        json.add("location", locationJson);

        json.addProperty("status", ticket.status().name());
        json.addProperty("message", ticket.message().message());

        return json;
    }

    public static JsonObject user(final @NonNull Soul soul) {
        JsonObject json = new JsonObject();

        json.addProperty("uuid", soul.uuid().toString());
        json.addProperty("name", soul.username());

        return json;
    }

}
