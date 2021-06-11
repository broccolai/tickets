package broccolai.tickets.core.configuration;

import com.zaxxer.hikari.HikariConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "CanBeFinal"})
public final class StorageConfiguration {

    private Type type = Type.H2;

    private String url = "jdbc:mysql://localhost:3306/tickets";

    private String username = "username";

    private String password = "********";

    public HikariConfig asHikari(final @NonNull Path folder) throws IOException {
        HikariConfig config = new HikariConfig();

        switch (this.type) {
            case H2 -> {
                Path file = folder.resolve("storage.db");
                if (!Files.exists(file)) {
                    Files.createFile(file);
                }
                config.setJdbcUrl("jdbc:h2:" + file.toAbsolutePath() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE");
                config.setDriverClassName("org.h2.Driver");
            }
            case MYSQL -> {
                config.setJdbcUrl(this.url);
                config.setUsername(this.username);
                config.setPassword(this.password);
            }
            default -> throw new IllegalStateException();
        }

        config.setMaximumPoolSize(10);
        return config;
    }

    private enum Type {
        H2,
        MYSQL
    }

}
