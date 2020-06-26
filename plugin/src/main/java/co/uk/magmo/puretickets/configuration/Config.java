package co.uk.magmo.puretickets.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class Config {
    String LOCALE;
    Integer REMINDER_DELAY;
    Integer REMINDER_REPEAT;

    Integer LIMIT_OPEN_TICKET;

    Boolean STORAGE_MYSQL;
    String STORAGE_USER;
    String STORAGE_PASSWORD;
    String STORAGE_NAME;
    String STORAGE_HOST;
    Boolean STORAGE_SSL;

    Boolean DISCORD_ENABLED;
    String DISCORD_GUILD;
    String DISCORD_TOKEN;

    String ALIAS_CREATE;
    String ALIAS_UPDATE;
    String ALIAS_CLOSE;
    String ALIAS_SHOW;
    String ALIAS_PICK;
    String ALIAS_ASSIGN;
    String ALIAS_DONE;
    String ALIAS_YIELD;
    String ALIAS_NOTE;
    String ALIAS_REOPEN;
    String ALIAS_TELEPORT;
    String ALIAS_LOG;
    String ALIAS_LIST;
    String ALIAS_STATUS;
    String ALIAS_HIGHSCORE;

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
    }
}
