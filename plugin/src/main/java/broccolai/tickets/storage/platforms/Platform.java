package broccolai.tickets.storage.platforms;

import broccolai.tickets.configuration.Config;
import co.aikar.idb.DbRow;
import org.bukkit.plugin.Plugin;

/**
 * Representation of functions that are specific to an sql implementation.
 */
public interface Platform {
    /**
     * Setup the platform.
     *
     * @param plugin the plugin instance to register
     * @param config the configuration instance to use
     */
    void setup(Plugin plugin, Config config);

    /**
     * Retrieve a long from a column.
     *
     * @param row    the database row to use
     * @param column the column to lookup
     * @return a long
     */
    Long getPureLong(DbRow row, String column);

    /**
     * Retrieve an integer from a object.
     *
     * @param value the object to transform
     * @return the integer value
     */
    Integer getPureInteger(Object value);
}
