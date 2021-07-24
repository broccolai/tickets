package broccolai.tickets.bukkit;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.context.ContextService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.bukkit.commands.BukkitTicketsCommand;
import broccolai.tickets.bukkit.context.BukkitTicketContextKeys;
import broccolai.tickets.bukkit.context.LocationContextSerializer;
import broccolai.tickets.bukkit.inject.BukkitModule;
import broccolai.tickets.bukkit.listeners.PlayerJoinListener;
import broccolai.tickets.bukkit.model.BukkitOnlineSoul;
import broccolai.tickets.bukkit.service.BukkitUserService;
import broccolai.tickets.bukkit.subscribers.TicketSubscriber;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.core.utilities.ArrayHelper;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.logging.Level;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public final class BukkitPlatform extends JavaPlugin implements PluginPlatform {

    private static final Class<? extends Listener>[] LISTENERS = ArrayHelper.create(
            PlayerJoinListener.class
    );

    private static final Class<? extends Subscriber>[] BUKKIT_SUBSCRIBERS = ArrayHelper.create(
            TicketSubscriber.class
    );

    private static final Class<? extends BaseCommand>[] BUKKIT_COMMANDS = ArrayHelper.create(
            BukkitTicketsCommand.class
    );

    private @MonotonicNonNull Injector injector;
    private @MonotonicNonNull PureTickets pureTickets;

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable() {
        this.getDataFolder().mkdirs();

        this.injector = Guice.createInjector(new BukkitModule(this, this));

        this.pureTickets = this.injector.getInstance(PureTickets.class);

        this.setupContexts();
        this.pureTickets.load();

        try {
            CommandManager<OnlineSoul> commandManager = this.commandManager(
                    this.injector.getInstance(UserService.class)
            );
            this.pureTickets.commands(commandManager, COMMANDS);
            this.pureTickets.commands(commandManager, BUKKIT_COMMANDS);
        } catch (final Exception e) {
            this.getLogger().log(Level.SEVERE, "Could not initiate Command Manager", e);
        }

        this.pureTickets.subscribers(BUKKIT_SUBSCRIBERS);

        for (final Class<? extends Listener> listenerClass : LISTENERS) {
            Listener listener = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }

    }

    @Override
    public void onDisable() {
        this.pureTickets.unload();
    }

    private CommandManager<OnlineSoul> commandManager(
            final @NonNull UserService userService
    ) throws Exception {
        BukkitUserService bukkitUserService = (BukkitUserService) userService;

        PaperCommandManager<@NonNull OnlineSoul> cloudManager = new PaperCommandManager<>(
                this,
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().withAsynchronousParsing().build(),
                sender -> {
                    if (sender instanceof ConsoleCommandSender) {
                        return userService.console();
                    }

                    Player player = (Player) sender;
                    return bukkitUserService.player(player);
                },
                soul -> {
                    BukkitOnlineSoul bukkitSoul = (BukkitOnlineSoul) soul;
                    return bukkitSoul.sender();
                }
        );

        if (cloudManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            cloudManager.registerAsynchronousCompletions();
        }

        this.pureTickets.defaultCommandManagerSettings(cloudManager);
        return cloudManager;
    }

    private void setupContexts() {
        ContextService contextService = this.injector.getInstance(ContextService.class);
        contextService.registerKeys(new BukkitTicketContextKeys());
        contextService.registerMapper(BukkitTicketContextKeys.LOCATION, new LocationContextSerializer());
    }

    @Override
    public ClassLoader loader() {
        return this.getClassLoader();
    }

}
