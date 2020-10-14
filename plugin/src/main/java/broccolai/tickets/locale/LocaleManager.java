package broccolai.tickets.locale;

import broccolai.corn.spigot.locale.CornLocaleManager;
import broccolai.corn.spigot.locale.LocaleKeys;
import broccolai.corn.spigot.locale.LocaleUtils;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LocaleManager extends CornLocaleManager {
    private LocaleManager(@NotNull FileConfiguration defaultSource, @NotNull LocaleKeys prefix) {
        super(defaultSource, prefix);
    }

    /**
     * Create a new LocaleManager.
     *
     * @param plugin Plugin instance
     * @return the created locale manager
     */
    @NotNull
    public static LocaleManager create(@NotNull final Plugin plugin) {
        Map<Locale, FileConfiguration> locales = LocaleUtils.saveLocales(plugin,
            new File(plugin.getDataFolder(), "locales"), "/locales");

        LocaleManager localeManager = new LocaleManager(locales.get(Locale.ENGLISH), Messages.GENERAL__PREFIX);
        localeManager.registerKeys(Messages.class);

        return localeManager;
    }
}
