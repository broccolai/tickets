package broccolai.tickets.storage;

import broccolai.tickets.configuration.Config;
import broccolai.tickets.message.Message;
import broccolai.tickets.storage.mapper.DateMapper;
import broccolai.tickets.storage.mapper.LocationMapper;
import broccolai.tickets.storage.mapper.MessageMapper;
import broccolai.tickets.storage.mapper.SettingsMapper;
import broccolai.tickets.storage.mapper.StatsMapper;
import broccolai.tickets.storage.mapper.TicketMapper;
import broccolai.tickets.storage.mapper.ValueDataMapper;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketIdStorage;
import broccolai.tickets.ticket.TicketStats;
import broccolai.tickets.user.UserSettings;
import broccolai.tickets.utilities.FileReader;
import com.google.gson.Gson;
import io.leangen.geantyref.TypeToken;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
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
     * @param plugin Plugin instance
     * @param config Configuration instance
     * @return Constructed Jdbi
     */
    public static @NonNull Jdbi construct(final @NonNull Plugin plugin, final @NonNull Config config) {
        final Jdbi jdbi;

        if (config.getStorageMySQL()) {
            jdbi = mysql(config);
        } else {
            jdbi = sqlite(plugin);
        }

        jdbi
                .registerRowMapper(Ticket.class, new TicketMapper())
                .registerRowMapper(Message.class, new MessageMapper())
                .registerRowMapper(UserSettings.class, new SettingsMapper())
                .registerRowMapper(TicketStats.class, new StatsMapper())
                .registerRowMapper(TicketIdStorage.ValueData.class, new ValueDataMapper())
                .registerColumnMapper(LocalDateTime.class, new DateMapper())
                .registerColumnMapper(Location.class, new LocationMapper());

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
                        plugin.getLogger().info("Migrating database to version " + migration.getVersion() + "...");
                        String queries = FileReader.fromPath(MIGRATION_PATH + migration.getPath());

                        for (final String query : queries.split(";")) {
                            if (query.trim().isEmpty()) {
                                continue;
                            }
                            handle.execute(query);
                        }

                        handle.execute(SQLQueries.UPDATE_VERSION.get(), migration.getVersion());
                    });
        });

        return jdbi;
    }

    private static @NonNull Jdbi mysql(final @NonNull Config config) {
        return Jdbi.create(
                "jdbc:mysql://" + config.getStorageHost() + "/" + config.getStorageName(),
                config.getStorageUser(),
                config.getStoragePassword()
        );
    }

    private static @NonNull Jdbi sqlite(final @NonNull Plugin plugin) {
        final File file = new File(plugin.getDataFolder(), "tickets.db");
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
