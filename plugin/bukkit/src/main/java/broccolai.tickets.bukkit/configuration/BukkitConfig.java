package broccolai.tickets.bukkit.configuration;

import broccolai.corn.spigot.locale.LocaleUtils;
import broccolai.tickets.bukkit.BukkitPlatform;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.configuration.Config;
import cloud.commandframework.types.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class BukkitConfig extends Config {

    /**
     * Initialise the Configuration wrapper with values.
     *
     * @param plugin the Plugin instance to use
     */
    public BukkitConfig(final @NonNull PureTickets<?, ?, ?> plugin) {
        super(plugin);
    }

    @Override
    protected void setup(final @NonNull PureTickets<?, ?, ?> pureTickets) {
        BukkitPlatform plugin = (BukkitPlatform) pureTickets.getPlatform();

        //noinspection rawtypes
        Class<Pair> pairClass = Pair.class;

        plugin.saveDefaultConfig();
        InputStream stream = plugin.getClass().getResourceAsStream("/config.yml");
        LocaleUtils.mergeYaml(stream, new File(plugin.getDataFolder(), "config.yml"));

        stream = plugin.getClass().getResourceAsStream("/config.yml");
        FileConfiguration config = plugin.getConfig();
        InputStreamReader streamReader = new InputStreamReader(stream);
        FileConfiguration sourceConfig = YamlConfiguration.loadConfiguration(streamReader);

        for (Field field : Config.class.getDeclaredFields()) {
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
