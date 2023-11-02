package broccolai.tickets.paper;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.paper.inject.PaperModule;
import broccolai.tickets.paper.listeners.PlayerJoinListener;
import broccolai.tickets.paper.model.PaperOnlineSoul;
import broccolai.tickets.paper.service.PaperUserService;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.core.utilities.ArrayHelper;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;

import cloud.commandframework.paper.PaperCommandManager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public final class PaperPlatform extends JavaPlugin implements PluginPlatform {

    private static final Class<? extends Listener>[] LISTENERS = ArrayHelper.create(
            PlayerJoinListener.class
    );

    private @MonotonicNonNull PureTickets pureTickets;

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable() {
        this.getDataFolder().mkdirs();

        Injector injector = Guice.createInjector(
                new PaperModule(this, this)
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

    private CommandManager<OnlineSoul> commandManager(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService
    ) throws Exception {
        PaperUserService paperUserService = (PaperUserService) userService;

        PaperCommandManager<@NonNull OnlineSoul> cloudManager = new PaperCommandManager<>(
                this,
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>builder().withAsynchronousParsing().build(),
                sender -> {
                    if (sender instanceof ConsoleCommandSender) {
                        return userService.console();
                    }

                    Player player = (Player) sender;
                    return paperUserService.player(player);
                },
                soul -> {
                    PaperOnlineSoul bukkitSoul = (PaperOnlineSoul) soul;
                    return bukkitSoul.sender();
                }
        );

        if (cloudManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            cloudManager.registerAsynchronousCompletions();
        }

        new MinecraftExceptionHandler<@NonNull OnlineSoul>()
                .withDefaultHandlers()
                .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, (ex) -> {
                    InvalidCommandSenderException icse = (InvalidCommandSenderException) ex;
                    return messageService.exceptionWrongSender(icse.getRequiredSender());
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, (ex) -> messageService.exceptionNoPermission())
                .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, (ex) -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_ARGUMENT_PARSING_FUNCTION.apply(ex);
                    }

                    return pureException.message(messageService);
                })
                .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, (ex) -> {
                    Throwable cause = ex.getCause();

                    if (!(cause instanceof PureException pureException)) {
                        return MinecraftExceptionHandler.DEFAULT_COMMAND_EXECUTION_FUNCTION.apply(ex);
                    }

                    return pureException.message(messageService);
                })
                .apply(cloudManager, soul -> soul);

        cloudManager.commandSuggestionProcessor((context, strings) -> {
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
