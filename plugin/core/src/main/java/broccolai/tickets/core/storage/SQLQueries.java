package broccolai.tickets.core.storage;

import broccolai.tickets.core.utilities.FileReader;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum SQLQueries {
    INSERT_TICKET,
    UPDATE_TICKET,
    SELECT_TICKETS,
    SELECT_TICKETS_STATUSES,
    SELECT_TICKETS_SOUL_STATUSES,
    SELECT_INTERACTIONS,
    INSERT_INTERACTION,
    INSERT_CONTEXT,
    NOTIFICATIONS,
    INSERT_NOTIFICATION,
    HIGHSCORES,
    SAVE_SNAPSHOTS,
    SNAPSHOT_NAME,
    SNAPSHOT_UUID;

    private final String query;

    SQLQueries() {
        String path = name().toLowerCase().replace('_', '-');
        this.query = FileReader.fromPath("/queries/" + path + ".sql");
    }

    public @NonNull String[] get() {
        return this.query.split(";");
    }

}
