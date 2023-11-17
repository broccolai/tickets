package love.broccolai.tickets.common.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.sql.DataSource;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.common.registry.MappedActionRegistry;
import love.broccolai.tickets.common.serialization.jdbi.ActionMapper;
import love.broccolai.tickets.common.serialization.jdbi.TicketMapper;
import org.jdbi.v3.core.Jdbi;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ActionRegistry.class).to(MappedActionRegistry.class);
    }

    @Provides
    @Singleton
    public DataSource provideDataSource(final Path folder) throws IOException {
        HikariConfig hikariConfig = new HikariConfig();

        Path file = folder.resolve("storage.db");

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setJdbcUrl("jdbc:h2:" + file.toAbsolutePath() + ";MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE");

        hikariConfig.setMaximumPoolSize(10);

        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(
        final DataSource dataSource,
        final ActionMapper actionMapper,
        final TicketMapper ticketMapper
    ) {
        return Jdbi.create(dataSource)
            .registerRowMapper(actionMapper)
            .registerRowMapper(ticketMapper);
    }

}
