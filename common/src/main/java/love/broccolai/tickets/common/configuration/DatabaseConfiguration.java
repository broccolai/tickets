package love.broccolai.tickets.common.configuration;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@NullMarked
@ConfigSerializable
public class DatabaseConfiguration implements Configuration {

    @Comment("Supported databases: H2 | postgres")
    public Type type = Type.H2;

    public H2 h2 = new H2();

    public Postgres postgres = new Postgres();

    @ConfigSerializable
    public static class H2 {
        public String url = "jdbc:h2:./plugins/tickets/storage.db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH";
    }

    @ConfigSerializable
    public static class Postgres {
        public String url = "jdbc:postgresql://localhost:5432/database";

        public String username = "username";

        public String password = "password";
    }

    public enum Type {
        H2,
        POSTGRES
    }
}
