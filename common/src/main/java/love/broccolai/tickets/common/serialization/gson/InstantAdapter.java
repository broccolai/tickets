package love.broccolai.tickets.common.serialization.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

//todo: could be better, rushed.
public final class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.value(value.toEpochMilli());
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        if (token == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return Instant.ofEpochMilli(in.nextLong());
    }
}
