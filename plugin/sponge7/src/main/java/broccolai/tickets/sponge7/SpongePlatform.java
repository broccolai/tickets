package broccolai.tickets.sponge7;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.sponge7.inject.SpongeModule;
import broccolai.tickets.sponge7.model.SpongeOnlineSoul;
import broccolai.tickets.sponge7.service.SpongeUserService;
import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.sponge7.SpongeCommandManager;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "PureTickets", name = "PureTickets", version = "5.1.0-SNAPSHOT", description = "Tickets Plugin")
public final class SpongePlatform implements PluginPlatform {

    private final Injector spongeInjector;
    private final Path configDir;

    private @MonotonicNonNull PureTickets pureTickets;

    @Inject
    public SpongePlatform(
            final @NonNull Injector injector,
            final @NonNull @ConfigDir(sharedRoot = false) Path configDir
    ) {
        this.spongeInjector = injector;
        this.configDir = configDir;
    }

    @Listener
    public void onServerStart(final @NonNull GameInitializationEvent event) {
        File configFolder = this.configDir.toFile();
        configFolder.mkdirs();

        Injector injector = Guice.createInjector(
                new SpongeModule(this)
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

    private CommandManager<OnlineSoul> commandManager(
            final @NonNull UserService userService,
            final @NonNull MessageService messageService
    ) throws Exception {
        SpongeUserService spongeUserService = (SpongeUserService) userService;

        SpongeCommandManager<@NonNull OnlineSoul> cloudManager = new SpongeCommandManager<>(
                Sponge.getPluginManager().fromInstance(this).orElseThrow(),
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().withAsynchronousParsing().build(),
                sender -> {
                    if (sender instanceof ConsoleSource) {
                        return userService.console();
                    }

                    Player player = (Player) sender;
                    return spongeUserService.player(player);
                },
                soul -> {
                    SpongeOnlineSoul bukkitSoul = (SpongeOnlineSoul) soul;
                    return bukkitSoul.sender();
                }
        );

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
        return null;
    }

}
