package love.broccolai.tickets.common.serialization.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

public final class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(final JsonWriter writer, final Instant value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        writer.value(value.toEpochMilli());
    }

    @Override
    public Instant read(final JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return Instant.ofEpochMilli(reader.nextLong());
    }
}
