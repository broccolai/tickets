package broccolai.tickets.velocity;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.velocity.inject.VelocityModule;
import broccolai.tickets.velocity.subscribers.PlayerJoinSubscriber;
import broccolai.tickets.velocity.service.VelocityUserService;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.exceptions.PureException;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.core.utilities.ArrayHelper;
import broccolai.tickets.velocity.subscribers.VelocitySubscriber;
import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public final class VelocityPlatform implements PluginPlatform {

    private static final Class<? extends VelocitySubscriber>[] LISTENERS = ArrayHelper.create(
            PlayerJoinSubscriber.class
    );

    private final Injector velocityInjector;
    private final ProxyServer server;
    private final Path directory;

    private @MonotonicNonNull PureTickets pureTickets;

    @Inject
    public VelocityPlatform(
            final @NonNull Injector velocityInjector,
            final @NonNull ProxyServer server,
            final @NonNull Path directory
    ) {
        this.velocityInjector = velocityInjector;
        this.server = server;
        this.directory = directory;
    }

    @Subscribe
    public void onInitialisation(final @NonNull ProxyInitializeEvent event) throws IOException {
        Files.createDirectories(this.directory);

        Injector injector = velocityInjector.createChildInjector(
                new VelocityModule(this, this.server, this.directory)
        );

        this.pureTickets = injector.getInstance(PureTickets.class);
        injector = this.pureTickets.load();

        try {
            CommandManager<OnlineSoul> commandManager = this.commandManager(injector);
            this.pureTickets.commands(commandManager, COMMANDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.pureTickets.subscribers(SUBSCRIBERS);

        for (final Class<? extends VelocitySubscriber> listenerClass : LISTENERS) {
            VelocitySubscriber listener = injector.getInstance(listenerClass);
            this.server.getEventManager().register(listener, this);
        }
    }

    @Subscribe
    public void onShutdown(final @NonNull ProxyShutdownEvent event) {
        this.pureTickets.unload();
    }

    @SuppressWarnings("OverrideOnly")
    private CommandManager<OnlineSoul> commandManager(
            final @NonNull Injector injector
    ) {
        MessageService messageService = injector.getInstance(MessageService.class);
        VelocityUserService velocityUserService = (VelocityUserService) injector.getInstance(UserService.class);

        VelocityCommandManager<@NonNull OnlineSoul> cloudManager = injector.getInstance(
                Key.get(new TypeLiteral<>() {})
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
        return this.getClass().getClassLoader();
    }

}
