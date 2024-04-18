package love.broccolai.tickets.spring.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import love.broccolai.tickets.api.model.action.Action;

public class ActionSerializer implements JsonSerializer<Action> {

    @Override
    public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src, src.getClass());

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String type = src.getClass().getSimpleName();
            jsonObject.addProperty("type", type);

            return jsonObject;
        } else {
            return jsonElement;
        }
    }
}
