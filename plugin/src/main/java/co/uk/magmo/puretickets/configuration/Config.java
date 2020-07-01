package co.uk.magmo.puretickets.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class Config {
    public String LOCALE;
    public Integer REMINDER_DELAY;
    public Integer REMINDER_REPEAT;

    public Integer LIMIT_OPEN_TICKET;

    public Boolean STORAGE_MYSQL;
    public String STORAGE_USER;
    public String STORAGE_PASSWORD;
    public String STORAGE_NAME;
    public String STORAGE_HOST;
    public Boolean STORAGE_SSL;

    public Boolean DISCORD_ENABLED;
    public String DISCORD_GUILD;
    public String DISCORD_TOKEN;

    public String ALIAS_CREATE;
    public String ALIAS_UPDATE;
    public String ALIAS_CLOSE;
    public String ALIAS_SHOW;
    public String ALIAS_PICK;
    public String ALIAS_ASSIGN;
    public String ALIAS_DONE;
    public String ALIAS_YIELD;
    public String ALIAS_NOTE;
    public String ALIAS_REOPEN;
    public String ALIAS_TELEPORT;
    public String ALIAS_LOG;
    public String ALIAS_LIST;
    public String ALIAS_STATUS;
    public String ALIAS_HIGHSCORE;

    public Config(Plugin plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        InputStream stream = plugin.getClass().getResourceAsStream("/config.yml");
        InputStreamReader streamReader = new InputStreamReader(stream);
        FileConfiguration sourceConfig = YamlConfiguration.loadConfiguration(streamReader);

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String fieldName = field.getName().toLowerCase().replace('_', '.');
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
