package broccolai.tickets.core.locale;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class LocaleManager {

    private LocaleManager(final @NonNull Object defaultSource, final @NonNull Object prefix) {
    }

    /**
     * Create a new LocaleManager.
     * <p>
     * //     * @param plugin Plugin instance
     *
     * @return the created locale manager
     */
    public static @NonNull LocaleManager create(/*final @NonNull Plugin plugin*/) {
//        Map<Locale, FileConfiguration> locales = LocaleUtils.saveLocales(plugin,
//                new File(plugin.getDataFolder(), "locales"), "/locales"
//        );
//
//        LocaleManager localeManager = new LocaleManager(locales.get(Locale.ENGLISH), Messages.GENERAL__PREFIX);
//        localeManager.registerKeys(Messages.class);

        return null;
    }

}
