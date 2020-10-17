package broccolai.tickets.user;

import broccolai.tickets.locale.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public final class ConsoleSoul extends Soul {

    public static final UUID CONSOLE_UUID = new UUID(0L, 0L);
    public static final CommandSender CONSOLE_SENDER = Bukkit.getConsoleSender();

    /**
     * Construct a ConsoleSoul
     *
     * @param localeManager Locale manager instance
     */
    public ConsoleSoul(final @NonNull LocaleManager localeManager) {
        super(localeManager);
    }

    @Override
    @NonNull
    public String getName() {
        return "CONSOLE";
    }

    @Override
    @NonNull
    public UUID getUniqueId() {
        return CONSOLE_UUID;
    }

    @Override
    @NonNull
    public CommandSender asSender() {
        return CONSOLE_SENDER;
    }

    @Override
    public void message(@NonNull final String message) {
        CONSOLE_SENDER.sendMessage(message);
    }

}
