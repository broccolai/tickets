package broccolai.tickets.core.configuration;

import com.zaxxer.hikari.HikariConfig;

import java.io.IOException;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public final class StorageConfiguration {

    private final Type type = Type.H2;

    private final String url = "jdbc:mysql://localhost:3306/tickets";

    private final String username = "username";

    private final String password = "********";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public HikariConfig asHikari(final @NonNull File folder) {
        HikariConfig config = new HikariConfig();

        switch (this.type) {
            case H2:
                File file = new File(folder, "storage.db");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                config.setJdbcUrl("jdbc:h2:" + file.toPath().toAbsolutePath() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE");
                config.setDriverClassName("org.h2.Driver");
                break;
            case MYSQL:
                config.setJdbcUrl(this.url);
                config.setUsername(this.username);
                config.setPassword(this.password);
                break;
            default:
                throw new IllegalStateException();
        }

        config.setMaximumPoolSize(10);
        return config;
    }

    private enum Type {
        H2,
        MYSQL
    }

}
