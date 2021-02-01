package broccolai.tickets.core.storage;

import broccolai.tickets.core.utilities.FileReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

public enum SQLQueries {
    INSERT_TICKET,
    INSERT_MESSAGE,
    INSERT_SETTINGS,
    INSERT_NOTIFICATION,
    UPDATE_VERSION,
    UPDATE_TICKET,
    UPDATE_SETTINGS,
    SELECT_VERSION,
    SELECT_UUIDS,
    SELECT_TICKET,
    SELECT_TICKETS,
    SELECT_IDS_STATUS,
    SELECT_IDS_UUID_STATUS,
    SELECT_LIMITED_IDS,
    SELECT_HIGHEST_ID,
    SELECT_HIGHEST_ID_WHERE,
    SELECT_TICKET_STATS,
    SELECT_TICKET_STATS_UUID,
    SELECT_HIGHSCORES,
    SELECT_SETTINGS,
    SELECT_NOTIFICATIONS,
    EXISTS_SETTINGS,
    COUNT_TICKETS,
    COUNT_TICKETS_UUID,
    DELETE_NOTIFICATIONS,
    PURGE_EVERYTHING;

    private final String query;

    SQLQueries() {
        String path = name().toLowerCase().replace('_', '-');
        this.query = FileReader.fromPath("/queries/" + path + ".sql");
    }

    /**
     * Get the query saved in the file
     *
     * @return Query
     */
    public @NonNull String get() {
        return query;
    }

    /**
     * Apply a consumer to each query inside the enum
     *
     * @param consumer Consumer to be applied to each query
     */
    public void forEach(final @NonNull Consumer<@NonNull String> consumer) {
        for (final @NonNull String query : this.query.split(";")) {
            if (query.trim().isEmpty()) {
                continue;
            }
            consumer.accept(query);
        }
    }
}
