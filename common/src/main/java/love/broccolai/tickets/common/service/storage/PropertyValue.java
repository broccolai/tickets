package love.broccolai.tickets.common.service.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import love.broccolai.tickets.api.model.Location;
import love.broccolai.tickets.api.model.profile.Profile;
import org.jdbi.v3.core.statement.Update;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record PropertyValue(
    Kind kind,
    Object value
) {

    public static PropertyValue of(final Object value) {
        return switch (value) {
            case String string -> new PropertyValue(Kind.STRING, string);
            case UUID uuid -> new PropertyValue(Kind.UUID, uuid);
            case Instant instant -> new PropertyValue(Kind.INSTANT, instant);
            case Boolean bool -> new PropertyValue(Kind.BOOLEAN, bool);
            case Byte number -> new PropertyValue(Kind.INTEGER, number.intValue());
            case Short number -> new PropertyValue(Kind.INTEGER, number.intValue());
            case Integer number -> new PropertyValue(Kind.INTEGER, number);
            case Long number -> new PropertyValue(Kind.LONG, number);
            case Float number -> new PropertyValue(Kind.FLOAT, number);
            case Double number -> new PropertyValue(Kind.DOUBLE, number);
            case Profile profile -> new PropertyValue(Kind.PROFILE, profile);
            case Location location -> new PropertyValue(Kind.LOCATION, location);
            case Enum<?> enumeration -> new PropertyValue(Kind.STRING, enumeration.name());
            default -> throw new IllegalArgumentException(
                "Unsupported ticket property value: " + value.getClass().getName()
            );
        };
    }

    static PropertyValue from(final ResultSet resultSet) throws SQLException {
        Kind kind = Kind.of(resultSet.getString("value_type"));
        Timestamp storedInstant = resultSet.getTimestamp("instant_value");
        Object value = switch (kind) {
            case STRING -> resultSet.getString("string_value");
            case UUID -> resultSet.getObject("uuid_value", UUID.class);
            case INSTANT -> storedInstant.toInstant();
            case BOOLEAN -> (Boolean) resultSet.getObject("boolean_value");
            case NUMBER, DOUBLE -> (Double) resultSet.getObject("number_value");
            case INTEGER -> resultSet.getObject("number_value", Double.class).intValue();
            case LONG -> resultSet.getObject("number_value", Double.class).longValue();
            case FLOAT -> resultSet.getObject("number_value", Double.class).floatValue();
            case PROFILE -> new Profile(
                resultSet.getObject("uuid_value", UUID.class),
                resultSet.getString("string_value")
            );
            case LOCATION -> PropertyValue.decodeLocation(resultSet.getString("string_value"));
        };

        return new PropertyValue(kind, value);
    }

    void bind(final Update update) {
        update
            .bind("value_type", this.kind.storageKey())
            .bind("string_value", this.stringValue())
            .bind("number_value", this.numberValue())
            .bind("boolean_value", this.booleanValue())
            .bindByType("uuid_value", this.uuidValue(), UUID.class)
            .bindByType("instant_value", this.instantValue(), Instant.class);
    }

    private @Nullable String stringValue() {
        return switch (this.kind) {
            case STRING -> (String) this.value;
            case PROFILE -> ((Profile) this.value).username();
            case LOCATION -> this.encode((Location) this.value);
            default -> null;
        };
    }

    private @Nullable Double numberValue() {
        return switch (this.kind) {
            case NUMBER, DOUBLE -> (Double) this.value;
            case INTEGER -> ((Integer) this.value).doubleValue();
            case LONG -> ((Long) this.value).doubleValue();
            case FLOAT -> ((Float) this.value).doubleValue();
            default -> null;
        };
    }

    private @Nullable Boolean booleanValue() {
        return this.kind == Kind.BOOLEAN ? (Boolean) this.value : null;
    }

    private @Nullable UUID uuidValue() {
        return switch (this.kind) {
            case UUID -> (UUID) this.value;
            case PROFILE -> ((Profile) this.value).uuid();
            default -> null;
        };
    }

    private @Nullable Instant instantValue() {
        return this.kind == Kind.INSTANT ? (Instant) this.value : null;
    }

    private String encode(final Location location) {
        return String.join(
            ";",
            location.world(),
            Double.toString(location.x()),
            Double.toString(location.y()),
            Double.toString(location.z()),
            Float.toString(location.yaw()),
            Float.toString(location.pitch())
        );
    }

    private static Location decodeLocation(final String value) {
        String[] locationParts = value.split(";");
        return new Location(
            locationParts[0],
            Double.parseDouble(locationParts[1]),
            Double.parseDouble(locationParts[2]),
            Double.parseDouble(locationParts[3]),
            Float.parseFloat(locationParts[4]),
            Float.parseFloat(locationParts[5])
        );
    }

    enum Kind {
        STRING("string"),
        UUID("uuid"),
        INSTANT("instant"),
        BOOLEAN("boolean"),
        NUMBER("number"),
        INTEGER("int"),
        LONG("long"),
        FLOAT("float"),
        DOUBLE("double"),
        PROFILE("profile"),
        LOCATION("location");

        private final String storageKey;

        Kind(final String storageKey) {
            this.storageKey = storageKey;
        }

        String storageKey() {
            return this.storageKey;
        }

        static Kind of(final String storageKey) {
            for (Kind kind : Kind.values()) {
                if (kind.storageKey.equals(storageKey)) {
                    return kind;
                }
            }

            throw new IllegalArgumentException("Unknown stored property type: " + storageKey);
        }
    }
}
