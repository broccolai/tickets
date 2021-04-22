package broccolai.tickets.bukkit;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.bukkit.inject.BukkitModule;
import broccolai.tickets.bukkit.listeners.PlayerJoinListener;
import broccolai.tickets.bukkit.model.BukkitOnlineSoul;
import broccolai.tickets.bukkit.service.BukkitUserService;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.core.utilities.ArrayHelper;
import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.audience.ForwardingAudience;
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

        for (final Class<? extends Listener> listenerClass : LISTENERS) {
            Listener listener = injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        this.pureTickets.unload();
    }

    @SuppressWarnings("OverrideOnly")
    private CommandManager<OnlineSoul> commandManager(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService
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

        cloudManager.registerAsynchronousCompletions();

        new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, (ex) -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    return messageService.exceptionWrongSender(icse.getRequiredSender());
                })
                .withHandler(
                        MinecraftExceptionHandler.ExceptionType.NO_PERMISSION,
                        (ex) -> messageService.exceptionNoPermission()
                )
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

        cloudManager.setCommandSuggestionProcessor((context, strings) -> {
            String input;

            if (context.getInputQueue().isEmpty()) {
                input = "";
            } else {
                input = context.getInputQueue().peek();
            }

            input = input.toLowerCase();
            List<String> suggestions = new LinkedList<>();

            for (String suggestion : strings) {
                suggestion = suggestion.toLowerCase();

                if (suggestion.startsWith(input)) {
                    suggestions.add(suggestion);
                }
            }

            return suggestions;
        });

        return cloudManager;
    }

    @Override
    public ClassLoader loader() {
        return this.getClassLoader();
    }

}
