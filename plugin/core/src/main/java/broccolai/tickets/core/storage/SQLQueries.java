package broccolai.tickets.core.storage;

import broccolai.tickets.core.utilities.FileReader;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum SQLQueries {
    INSERT_TICKET,
    UPDATE_TICKET,
    COUNT_TICKETS_STATUSES,
    SELECT_TICKETS,
    SELECT_TICKETS_STATUSES,
    SELECT_TICKETS_SOUL_STATUSES,
    INSERT_INTERACTION;

    private final String query;

    SQLQueries() {
        String path = name().toLowerCase().replace('_', '-');
        this.query = FileReader.fromPath("/queries/" + path + ".sql");
    }

    public @NonNull String[] get() {
        return this.query.split(";");
    }

}
