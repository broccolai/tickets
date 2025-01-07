package love.broccolai.tickets.common.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.model.format.TicketFormatStyle;
import love.broccolai.tickets.api.utilities.Pair;

public class TicketFormatContentAdapter extends TypeAdapter<TicketFormatContent> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, TicketFormatContent content) throws IOException {
        out.beginObject();
        for (var entry : content.entrySet()) {
            String key = entry.getKey();
            out.name(key);
            out.beginObject();

            TicketFormatStyle style = entry.getValue().first();
            Object value = entry.getValue().second();

            out.name("style");
            out.value(style.name());

            out.name("value");
            if (value == null) {
                out.nullValue();
            } else {
                this.gson.toJson(value, value.getClass(), out);
            }

            out.endObject();
        }
        out.endObject();
    }

    @Override
    public TicketFormatContent read(JsonReader in) throws IOException {
        TicketFormatContent content = new TicketFormatContent();
        in.beginObject();

        while (in.hasNext()) {
            String entryName = in.nextName();
            in.beginObject();

            TicketFormatStyle style = null;
            Object value = null;

            while (in.hasNext()) {
                String fieldName = in.nextName();
                switch (fieldName) {
                    case "style" -> {
                        String styleName = in.nextString();
                        style = TicketFormatStyle.valueOf(styleName);
                    }
                    case "value" -> {
                        JsonElement valueElement = JsonParser.parseReader(in);
                        if (style != null) {
                            Class<?> type = style.contentType();
                            value = this.gson.fromJson(valueElement, type);
                        } else {
                            value = valueElement;
                        }
                    }
                    default -> in.skipValue();
                }
            }

            in.endObject();

            content.put(entryName, Pair.of(style, value));
        }

        in.endObject();
        return content;
    }
}
