package broccolai.tickets.storage.platforms;

import broccolai.tickets.configuration.Config;
import co.aikar.idb.DbRow;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Representation of functions that are specific to an sql implementation
 */
public interface Platform {

    /**
     * Setup the platform
     *
     * @param plugin Plugin instance
     * @param config Configuration instance
     */
    void setup(@NonNull Plugin plugin, @NonNull Config config);

    /**
     * Retrieve a long from a column
     *
     * @param row    Database row
     * @param column Database column
     * @return Long
     */
    Long getPureLong(@NonNull DbRow row, @NonNull String column);

    /**
     * Retrieve an integer from a object
     *
     * @param value Object
     * @return Integer value
     */
    Integer getPureInteger(@NonNull Object value);

}
