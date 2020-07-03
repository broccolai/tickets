package co.uk.magmo.puretickets.configuration;

import co.uk.magmo.puretickets.utilities.FileUtilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

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

    public String ALIAS__CREATE;
    public String ALIAS__UPDATE;
    public String ALIAS__CLOSE;
    public String ALIAS__SHOW;
    public String ALIAS__PICK;
    public String ALIAS__ASSIGN;
    public String ALIAS__DONE;
    public String ALIAS__YIELD;
    public String ALIAS__NOTE;
    public String ALIAS__REOPEN;
    public String ALIAS__TELEPORT;
    public String ALIAS__LOG;
    public String ALIAS__LIST;
    public String ALIAS__STATUS;
    public String ALIAS__HIGHSCORE;

    public Config(Plugin plugin) {
        plugin.saveDefaultConfig();
        InputStream stream = plugin.getClass().getResourceAsStream("/config.yml");

        FileUtilities.mergeYaml(stream, new File(plugin.getDataFolder(), "config.yml"));

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
