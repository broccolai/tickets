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
import broccolai.tickets.core.utilities.FileReader;
import broccolai.tickets.core.utilities.TicketLocation;
import com.google.gson.Gson;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public final class SQLPlatforms {

    private static final Gson GSON = new Gson();
    private static final String MIGRATION_PATH = "/queries/migrations/";

    private SQLPlatforms() {
    }

    /**
     * @param rootFolder Root folder to look in when using sqlite
     * @param config     Configuration instance
     * @return Constructed Jdbi
     */
    public static @NonNull Jdbi construct(
            final @NonNull File rootFolder,
            final @NonNull Config config
    ) {
        final Jdbi jdbi;

        if (config.getStorageMySQL()) {
            jdbi = mysql(config);
        } else {
            jdbi = sqlite(rootFolder);
        }

        String jsonData = FileReader.fromPath(MIGRATION_PATH + "migrations-index.json");

        List<MigrationEntry> migrations = GSON
                .fromJson(
                        jsonData,
                        new TypeToken<List<MigrationEntry>>() {
                        }.getType()
                );

        jdbi.useTransaction(handle -> {
            SQLQueries.CREATE_META.forEach(handle::execute);

            int version = handle.createQuery(SQLQueries.SELECT_VERSION.get())
                    .mapTo(int.class)
                    .findOne()
                    .orElse(0);

            migrations.stream()
                    .filter(migration -> migration.getVersion() > version)
                    .sorted(Comparator.comparingInt(MigrationEntry::getVersion))
                    .forEach(migration -> {
                        // todo:
                        System.out.println("Migrating database to version " + migration.getVersion() + "...");
                        String queries = FileReader.fromPath(MIGRATION_PATH + migration.getPath());

                        for (final String query : queries.split(";")) {
                            if (query.trim().isEmpty()) {
                                continue;
                            }
                            handle.execute(query);
                        }

                        handle.createUpdate(SQLQueries.UPDATE_VERSION.get())
                            .bind("version", migration.getVersion())
                            .execute();
                    });
        });

        return jdbi;
    }

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

    private static @NonNull Jdbi mysql(final @NonNull Config config) {
        return Jdbi.create(
                "jdbc:mysql://" + config.getStorageHost() + "/" + config.getStorageName(),
                config.getStorageUser(),
                config.getStoragePassword()
        );
    }

    private static @NonNull Jdbi sqlite(final @NonNull File rootFolder) {
        final File file = new File(rootFolder, "tickets.db");
        return Jdbi.create("jdbc:sqlite:" + file.toString());
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
