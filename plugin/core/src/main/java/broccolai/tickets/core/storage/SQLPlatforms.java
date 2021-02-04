package broccolai.tickets.core.storage;

import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.message.Message;
import broccolai.tickets.core.storage.mapper.ComponentMapper;
import broccolai.tickets.core.storage.mapper.DateMapper;
import broccolai.tickets.core.storage.mapper.LocationMapper;
import broccolai.tickets.core.storage.mapper.MessageMapper;
import broccolai.tickets.core.storage.mapper.SettingsMapper;
import broccolai.tickets.core.storage.mapper.StatsMapper;
import broccolai.tickets.core.storage.mapper.TicketMapper;
import broccolai.tickets.core.storage.mapper.ValueDataMapper;
import broccolai.tickets.core.ticket.Ticket;
import broccolai.tickets.core.ticket.TicketIdStorage;
import broccolai.tickets.core.ticket.TicketStats;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.user.UserSettings;
import broccolai.tickets.core.utilities.TicketLocation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.time.LocalDateTime;
import javax.sql.DataSource;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

public final class SQLPlatforms {

    private SQLPlatforms() {
    }

    /**
     * @param loader     Temp
     * @param rootFolder Root folder to look in when using sqlite
     * @param config     Configuration instance
     * @return Constructed Jdbi
     */
    public @NonNull
    static Jdbi construct(
            final @NonNull ClassLoader loader,
            final @NonNull File rootFolder,
            final @NonNull Config config
    ) {
        final boolean isMySQL = config.getStorageMySQL();
        final DataSource source;

        if (isMySQL) {
            source = mysql(config);
        } else {
            source = sqlite(rootFolder);
        }

        final Jdbi jdbi = Jdbi.create(source);

        Flyway flyway = Flyway.configure(loader)
                .baselineVersion("0")
                .baselineOnMigrate(true)
                .locations("queries/migrations")
                .dataSource(source)
                .load();

        flyway.migrate();

        return jdbi;
    }

    /**
     * Setup type mappers for JDBI
     *
     * @param jdbi        Jdbi instance
     * @param userManager User manager
     */
    public static void setupMappers(final @NonNull Jdbi jdbi, final @NonNull UserManager<?, ?, ?> userManager) {
        jdbi
                .registerRowMapper(Ticket.class, new TicketMapper(userManager))
                .registerRowMapper(Message.class, new MessageMapper())
                .registerRowMapper(UserSettings.class, new SettingsMapper())
                .registerRowMapper(TicketStats.class, new StatsMapper())
                .registerRowMapper(TicketIdStorage.ValueData.class, new ValueDataMapper())
                .registerColumnMapper(LocalDateTime.class, new DateMapper())
                .registerColumnMapper(Component.class, new ComponentMapper())
                .registerColumnMapper(TicketLocation.class, new LocationMapper());
    }

    private static @NonNull DataSource mysql(final @NonNull Config config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getStorageHost() + "/" + config.getStorageName());
        hikariConfig.setUsername(config.getStorageUser());
        hikariConfig.setPassword(config.getStoragePassword());
        hikariConfig.setMaximumPoolSize(10);

        return new HikariDataSource(hikariConfig);
    }

    private static @NonNull DataSource sqlite(final @NonNull File rootFolder) {
        final File file = new File(rootFolder, "tickets.db");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + file.toString());
        hikariConfig.setMaximumPoolSize(10);

        return new HikariDataSource(hikariConfig);
    }

    @SuppressWarnings("unused")
    public static final class MigrationEntry {

        private int version;
        private String path;

        /**
         * Construct migration entry
         */
        public MigrationEntry() {
        }

        /**
         * @return The version in the database this migration represents.
         */
        public int getVersion() {
            return this.version;
        }

        /**
         * @return The path for the migration SQL file.
         */
        public String getPath() {
            return this.path;
        }

    }

}
