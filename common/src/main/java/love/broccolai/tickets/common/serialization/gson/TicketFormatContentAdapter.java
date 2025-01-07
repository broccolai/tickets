package love.broccolai.tickets.common.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.model.format.TicketFormatContent;
import love.broccolai.tickets.api.model.format.TicketFormatPart;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;

public final class TicketFormatContentAdapter extends TypeAdapter<TicketFormatContent> {

    private final TicketTypeRegistry ticketTypeRegistry;
    private final Gson gson = new Gson();

    @Inject
    public TicketFormatContentAdapter(final TicketTypeRegistry ticketTypeRegistry) {
        this.ticketTypeRegistry = ticketTypeRegistry;
    }

    @Override
    public void write(JsonWriter out, TicketFormatContent content) throws IOException {
        out.beginObject();

        // 1) Write the formatIdentifier
        out.name("formatIdentifier").value(content.formatIdentifier());

        // 2) Write the parts map
        out.name("parts");
        out.beginObject();

        // Look up the ticket format so we know how to serialize each key
        TicketFormat format = this.ticketTypeRegistry.fromIdentifier(content.formatIdentifier());
        // (Could be null if unknown, so handle carefully)

        // Go through each entry in parts
        content.forEach((partKey, partValue) -> {
            try {
                out.name(partKey);
                if (format != null) {
                    TicketFormatPart ticketPart = format.findPart(partKey);
                    // We have a known style => we know the real Java type
                    Class<?> realType = ticketPart.style().contentType();
                    this.gson.toJson(partValue, realType, out);
                    return;
                }
                // Fallback if we can’t find a matching style
                this.gson.toJson(partValue, Object.class, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        out.endObject();
        out.endObject();
    }

    @Override
    public TicketFormatContent read(JsonReader in) throws IOException {
        // We’ll buffer the data in a local map first
        String formatIdentifier = null;
        Map<String, Object> parts = new HashMap<>();

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "formatIdentifier" -> {
                    formatIdentifier = in.nextString();
                }
                case "parts" -> {
                    in.beginObject();
                    // Look up the format for correct deserialization
                    TicketFormat format = this.ticketTypeRegistry.fromIdentifier(formatIdentifier);

                    while (in.hasNext()) {
                        String partKey = in.nextName();
                        // Attempt to find the part definition for this key
                        TicketFormatPart ticketPart = (format != null) ? format.findPart(partKey) : null;
                        Class<?> realType = (ticketPart != null)
                            ? ticketPart.style().contentType()
                            : Object.class; // fallback

                        Object partValue = this.gson.fromJson(in, realType);
                        parts.put(partKey, partValue);
                    }
                    in.endObject();
                }
                default -> {
                    // Skip anything unknown
                    in.skipValue();
                }
            }
        }
        in.endObject();

        // Finally, create a new TicketFormatContent
        TicketFormatContent content = new TicketFormatContent(formatIdentifier);
        parts.forEach(content::put);
        return content;
    }
}
