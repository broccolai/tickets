package broccolai.tickets.locale;

import broccolai.corn.spigot.locale.CornLocaleManager;
import broccolai.corn.spigot.locale.LocaleKeys;
import broccolai.corn.spigot.locale.LocaleUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public final class LocaleManager extends CornLocaleManager {

    private LocaleManager(final @NonNull FileConfiguration defaultSource, final @NonNull LocaleKeys prefix) {
        super(defaultSource, prefix);
    }

    /**
     * Create a new LocaleManager.
     *
     * @param plugin Plugin instance
     * @return the created locale manager
     */
    public static @NonNull LocaleManager create(final @NonNull Plugin plugin) {
        Map<Locale, FileConfiguration> locales = LocaleUtils.saveLocales(plugin,
                new File(plugin.getDataFolder(), "locales"), "/locales"
        );

        LocaleManager localeManager = new LocaleManager(locales.get(Locale.ENGLISH), Messages.GENERAL__PREFIX);
        localeManager.registerKeys(Messages.class);

        return localeManager;
    }

}
