package broccolai.tickets.bukkit.context;

import broccolai.tickets.api.model.context.ContextMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.io.IOException;
import java.util.UUID;

public final class LocationContextSerializer implements ContextMapper<Location> {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationAdapter().nullSafe())
            .create();

    @Override
    public @Nullable String serialize(final @Nullable Location value) {
        return GSON.toJson(value);
    }

    @Override
    public @Nullable Location deserialize(final @Nullable String value) {
        return GSON.fromJson(value, Location.class);
    }

    static class LocationAdapter extends TypeAdapter<Location> {

        private static final String WORLD = "world";
        private static final String X = "x";
        private static final String Y = "y";
        private static final String Z = "z";

        @Override
        public void write(final JsonWriter writer, final Location location) throws IOException {
            writer.beginObject();

            World world = location.getWorld();
            if (world != null) {
                writer.name(WORLD);
                writer.value(world.getUID().toString());
            }

            writer.name(X);
            writer.value(location.getBlockX());
            writer.name(Y);
            writer.value(location.getBlockY());
            writer.name(Z);
            writer.value(location.getBlockZ());

            writer.endObject();
        }

        @Override
        public Location read(final JsonReader reader) throws IOException {
            UUID worldUUID = null;
            int x = 0;
            int y = 0;
            int z = 0;

            reader.beginObject();

            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case WORLD:
                        worldUUID = UUID.fromString(reader.nextString());
                        break;
                    case X:
                        x = reader.nextInt();
                        break;
                    case Y:
                        y = reader.nextInt();
                        break;
                    case Z:
                        z = reader.nextInt();
                        break;
                    default:
                        break;
                }
            }

            reader.endObject();
            return new Location(worldUUID != null ? Bukkit.getWorld(worldUUID) : null, x, y, z);
        }

    }

}

