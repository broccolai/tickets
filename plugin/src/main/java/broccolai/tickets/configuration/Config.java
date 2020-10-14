package broccolai.tickets.configuration;

import broccolai.corn.spigot.locale.LocaleUtils;
import cloud.commandframework.types.tuples.Pair;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Wrapper for the Plugins Configuration.
 */
public class Config {
    public String LOCALE;
    public Integer REMINDER__DELAY;
    public Integer REMINDER__REPEAT;

    public Integer LIMIT__OPEN_TICKETS;

    public Boolean STORAGE__MYSQL;
    public String STORAGE__USER;
    public String STORAGE__PASSWORD;
    public String STORAGE__NAME;
    public String STORAGE__HOST;
    public Boolean STORAGE__SSL;

    public Boolean DISCORD__ENABLED;
    public String DISCORD__GUILD;
    public String DISCORD__TOKEN;
    public String DISCORD__NAME;

    public Pair<String, String[]> ALIAS__CREATE;
    public Pair<String, String[]> ALIAS__UPDATE;
    public Pair<String, String[]> ALIAS__CLOSE;
    public Pair<String, String[]> ALIAS__SHOW;
    public Pair<String, String[]> ALIAS__PICK;
    public Pair<String, String[]> ALIAS__ASSIGN;
    public Pair<String, String[]> ALIAS__DONE;
    public Pair<String, String[]> ALIAS__YIELD;
    public Pair<String, String[]> ALIAS__NOTE;
    public Pair<String, String[]> ALIAS__REOPEN;
    public Pair<String, String[]> ALIAS__TELEPORT;
    public Pair<String, String[]> ALIAS__LOG;
    public Pair<String, String[]> ALIAS__LIST;
    public Pair<String, String[]> ALIAS__STATUS;
    public Pair<String, String[]> ALIAS__HIGHSCORE;

    public String API__DOMAIN;

    /**
     * Initialise the Configuration wrapper with values.
     *
     * @param plugin the Plugin instance to use
     */
    public Config(Plugin plugin) {
        //noinspection rawtypes
        Class<Pair> pairClass = Pair.class;

        plugin.saveDefaultConfig();
        InputStream stream = plugin.getClass().getResourceAsStream("/config.yml");
        LocaleUtils.mergeYaml(stream, new File(plugin.getDataFolder(), "config.yml"));

        stream = plugin.getClass().getResourceAsStream("/config.yml");
        FileConfiguration config = plugin.getConfig();
        InputStreamReader streamReader = new InputStreamReader(stream);
        FileConfiguration sourceConfig = YamlConfiguration.loadConfiguration(streamReader);

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String fieldName = field.getName().toLowerCase().replace("__", ".");
            Object targetValue = config.get(fieldName, null);

            if (targetValue == null) {
                targetValue = sourceConfig.get(fieldName);
            }

            try {
                if (field.getType().isAssignableFrom(pairClass)) {
                    String unsplit = (String) targetValue;
                    String[] split = unsplit.split("\\|");
                    targetValue = Pair.of(split[0], Arrays.copyOfRange(split, 1, split.length));
                }

                field.set(this, targetValue);
            } catch (IllegalAccessException ignored) {
                Bukkit.getLogger().warning("PureTickets failed to set the " + fieldName + " configuration value");
            }
        }

        try {
            stream.close();
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("PureTickets could not close stream");
        }
    }
}
