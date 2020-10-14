package broccolai.tickets.user;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleSoul implements Soul {
    public static final UUID CONSOLE_UUID = new UUID(0L, 0L);
    public static final CommandSender CONSOLE_SENDER = Bukkit.getConsoleSender();

    @Override
    @NotNull
    public String getName() {
        return "CONSOLE";
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return CONSOLE_UUID;
    }

    @Override
    @Nullable
    public CommandSender asSender() {
        return CONSOLE_SENDER;
    }

    @Override
    public void message(@NotNull String message) {
        CONSOLE_SENDER.sendMessage(message);
    }
}
