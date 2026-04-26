package love.broccolai.tickets.common.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;

public final class TicketFormDataAdapter extends TypeAdapter<TicketFormData> {

    private final TicketTypeRegistry ticketTypeRegistry;
    private final Gson gson = new Gson();

    @Inject
    public TicketFormDataAdapter(final TicketTypeRegistry ticketTypeRegistry) {
        this.ticketTypeRegistry = ticketTypeRegistry;
    }

    @Override
    public void write(final JsonWriter writer, final TicketFormData data) throws IOException {
        writer.beginObject();
        writer.name("formatIdentifier").value(data.formatIdentifier());
        writer.name("parts");
        writer.beginObject();

        TicketFormat format = this.ticketTypeRegistry.findByIdentifier(data.formatIdentifier()).orElse(null);

        for (Entry<String, Object> entry : data.parts().entrySet()) {
            writer.name(entry.getKey());

            if (format != null) {
                Class<?> realType = format.requirePart(entry.getKey()).style().contentType();
                this.gson.toJson(entry.getValue(), realType, writer);
            } else {
                this.gson.toJson(entry.getValue(), Object.class, writer);
            }
        }

        writer.endObject();
        writer.endObject();
    }

    @Override
    public TicketFormData read(final JsonReader reader) throws IOException {
        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
        JsonElement formatElement = object.get("formatIdentifier");

        if (formatElement == null) {
            throw new IOException("Ticket form data is missing formatIdentifier");
        }

        String formatIdentifier = formatElement.getAsString();
        Map<String, Object> parts = new HashMap<>();
        JsonObject partsObject = object.getAsJsonObject("parts");

        if (partsObject != null) {
            TicketFormat format = this.ticketTypeRegistry.findByIdentifier(formatIdentifier).orElse(null);

            for (Entry<String, JsonElement> entry : partsObject.entrySet()) {
                Class<?> realType = format != null
                    ? format.requirePart(entry.getKey()).style().contentType()
                    : Object.class;

                parts.put(entry.getKey(), this.gson.fromJson(entry.getValue(), realType));
            }
        }

        return new TicketFormData(formatIdentifier, parts);
    }
}
