package broccolai.tickets.bukkit;

import broccolai.tickets.bukkit.commands.BukkitTicketsCommandManager;
import broccolai.tickets.bukkit.configuration.BukkitConfig;
import broccolai.tickets.bukkit.tasks.BukkitTaskManager;
import broccolai.tickets.bukkit.user.BukkitPlayerSoul;
import broccolai.tickets.bukkit.user.BukkitUserManager;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.TicketsPlatform;
import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.tasks.TaskManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@SuppressWarnings("unused")
public final class BukkitPlatform extends JavaPlugin implements TicketsPlatform<CommandSender, Player, BukkitPlayerSoul> {

    private final PureTickets<CommandSender, Player, BukkitPlayerSoul> pureTickets;

    /**
     * Create Bukkit platform
     */
    public BukkitPlatform() {
        this.pureTickets = new PureTickets<>(this);
    }

    @Override
    public void onEnable() {
        this.pureTickets.start();
    }

    @Override
    public void onDisable() {
        this.pureTickets.stop();
    }

    @Override
    public Config getConfig(@NonNull final PureTickets<?, ?, ?> pureTickets) {
        return new BukkitConfig(pureTickets);
    }

    @Override
    public void setupMessages(final @NonNull Config config) {
        String locale = config.getLocale();
        File localeFolder = new File(this.getDataFolder(), "locale");

        //noinspection ResultOfMethodCallIgnored
        localeFolder.mkdirs();
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(localeFolder, "locale_" + locale + ".yml"));

        Message.setup(configuration::getString);
    }

    @Override
    public BukkitUserManager getUserManager(
            @NonNull final TicketsEventBus eventManager,
            @NonNull final TaskManager taskManager,
            @NonNull final Jdbi jdbi
    ) {
        BukkitUserManager bukkitUserManager = new BukkitUserManager(this, eventManager, taskManager, jdbi);
        this.getServer().getPluginManager().registerEvents(bukkitUserManager, this);

        return bukkitUserManager;
    }

    @Override
    public @NonNull String getVersion() {
        return this.getDescription().getVersion();
    }

    @Override
    public @NotNull File getRootFolder() {
        return this.getDataFolder();
    }

    @Override
    public @NotNull TicketsCommandManager<CommandSender> getCommandManager() {
        return new BukkitTicketsCommandManager(this);
    }

    @Override
    public @NonNull TaskManager getTaskManager() {
        return new BukkitTaskManager(this);
    }

}
