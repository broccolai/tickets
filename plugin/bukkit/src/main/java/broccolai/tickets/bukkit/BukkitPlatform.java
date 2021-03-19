package broccolai.tickets.bukkit;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.bukkit.inject.BukkitModule;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;

import cloud.commandframework.paper.PaperCommandManager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public final class BukkitPlatform extends JavaPlugin implements PluginPlatform {

    private @MonotonicNonNull PureTickets pureTickets;

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(
                new BukkitModule(this, this)
        );

        this.pureTickets = injector.getInstance(PureTickets.class);
        injector = this.pureTickets.load();

        try {
            CommandManager<OnlineSoul> commandManager = this.commandManager(
                    injector.getInstance(UserService.class),
                    injector.getInstance(MessageService.class)
            );
            this.pureTickets.commands(commandManager, COMMANDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.pureTickets.subscribers(SUBSCRIBERS);
    }

    @Override
    public void onDisable() {
        this.pureTickets.unload();
    }

    private CommandManager<OnlineSoul> commandManager(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService
    ) throws Exception {
        PaperCommandManager<@NonNull OnlineSoul> cloudManager = new PaperCommandManager<>(
                this,
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().withAsynchronousParsing().build(),
                sender -> {
                    if (sender instanceof ConsoleCommandSender) {
                        return userService.console();
                    }

                    Player player = (Player) sender;
                    return userService.player(player.getUniqueId());
                },
                user -> Bukkit.getPlayer(user.uuid())
        );

        cloudManager.registerAsynchronousCompletions();

        new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, (ex) -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    return messageService.exceptionWrongSender(icse.getRequiredSender());
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (ex) -> messageService.exceptionNoPermission())
                .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, (ex) -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException)) {
                        return MinecraftExceptionHandler.DEFAULT_ARGUMENT_PARSING_FUNCTION.apply(ex);
                    }

                    PureException pureException = (PureException) cause;
                    return pureException.message(messageService);
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, (ex) -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException)) {
                        return MinecraftExceptionHandler.DEFAULT_COMMAND_EXECUTION_FUNCTION.apply(ex);
                    }

                    PureException pureException = (PureException) cause;
                    return pureException.message(messageService);
                })
                .apply(cloudManager, ForwardingAudience.Single::audience);

        return cloudManager;
    }

    @Override
    public ClassLoader loader() {
        return this.getClassLoader();
    }

}
