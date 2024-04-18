package love.broccolai.tickets.common.inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import javax.sql.DataSource;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import love.broccolai.tickets.common.configuration.Configuration;
import love.broccolai.tickets.common.configuration.DatabaseConfiguration;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import love.broccolai.tickets.common.registry.SimpleActionRegistry;
import love.broccolai.tickets.common.registry.SimpleTicketTypeRegistry;
import love.broccolai.tickets.common.serialization.gson.InstantAdapter;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketTypeMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.gson2.Gson2Config;
import org.jdbi.v3.gson2.Gson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;

@NullMarked
public final class ConfigurationModule extends AbstractModule {

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantAdapter())
        .create();

    @Override
    protected void configure() {
        this.bind(ActionRegistry.class).to(SimpleActionRegistry.class);
        this.bind(TicketTypeRegistry.class).to(SimpleTicketTypeRegistry.class);
    }

    @Provides
    @Singleton
    public DataSource provideDataSource(
        final DatabaseConfiguration configuration
    ) throws IOException {
        HikariConfig hikariConfig = new HikariConfig();

        switch (configuration.type) {
            case H2 -> {
                hikariConfig.setDriverClassName("org.h2.Driver");
                hikariConfig.setJdbcUrl(configuration.h2.url);
            }
            case POSTGRES -> {
                hikariConfig.setDriverClassName("org.postgresql.Driver");
                hikariConfig.setJdbcUrl(configuration.postgres.url);
                hikariConfig.setUsername(configuration.postgres.username);
                hikariConfig.setPassword(configuration.postgres.password);
            }
        }

        hikariConfig.setMaximumPoolSize(10);

        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(
        final DataSource dataSource,
        final ActionMapper actionMapper,
        final TicketMapper ticketMapper,
        final TicketTypeMapper ticketTypeMapper
    ) {
        Jdbi jdbi = Jdbi.create(dataSource)
            .installPlugin(new Gson2Plugin())
            .installPlugin(new PostgresPlugin())
            .registerRowMapper(actionMapper)
            .registerRowMapper(ticketMapper)
            .registerColumnMapper(ticketTypeMapper)
            .registerArgument(ticketTypeMapper);

        jdbi.getConfig(Gson2Config.class).setGson(GSON);

        return jdbi;
    }

    @Provides
    @Singleton
    public TicketsConfiguration provideTicketsConfiguration(final Path folder) throws IOException {
        Path file = folder.resolve("tickets.conf");

        return this.configuration(TicketsConfiguration.class, file);
    }

    @Provides
    @Singleton
    public DatabaseConfiguration provideDatabaseConfiguration(final Path folder) throws IOException {
        Path file = folder.resolve("database.conf");

        return this.configuration(DatabaseConfiguration.class, file);
    }

    private <T extends Configuration> T configuration(
        final Class<T> clazz,
        final Path file
    ) throws IOException {
        ObjectMapper<T> MAPPER = ObjectMapper.factory().get(clazz);

        if (Files.notExists(file)) {
            Files.createFile(file);
        }

        ConfigurationLoader<?> loader = this.pathConfigurationLoader(file);

        ConfigurationNode node = loader.load();
        T config = MAPPER.load(node);

        MAPPER.save(config, node);
        loader.save(node);

        return config;
    }

    private ConfigurationLoader<?> pathConfigurationLoader(final Path path) {
        return HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true))
            .path(path)
            .build();
    }
}
