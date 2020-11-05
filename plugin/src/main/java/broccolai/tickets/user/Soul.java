package broccolai.tickets.user;

import broccolai.corn.spigot.Recipient;
import broccolai.tickets.locale.LocaleManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public abstract class Soul implements Recipient {

    @NonNull
    private final LocaleManager localeManager;

    /**
     * Construct a new Soul
     *
     * @param localeManager Locale Manager instance
     */
    public Soul(final @NonNull LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public final @NonNull LocaleManager getLocaleManager() {
        return localeManager;
    }

    /**
     * Get the souls name
     *
     * @return Name
     */
    public abstract @NonNull String getName();

    /**
     * Get the souls unique id
     *
     * @return Unique id
     */
    public abstract @NonNull UUID getUniqueId();

    /**
     * Get the Command Sender associated with the soul
     *
     * @return Command Sender
     */
    public abstract @Nullable CommandSender asSender();

}
