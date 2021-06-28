package broccolai.tickets.velocity;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.velocity.inject.VelocityModule;
import broccolai.tickets.velocity.model.VelocityOnlineSoul;
import broccolai.tickets.velocity.subscribers.PlayerJoinSubscriber;
import broccolai.tickets.velocity.service.VelocityUserService;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.core.utilities.ArrayHelper;
import broccolai.tickets.velocity.subscribers.VelocitySubscriber;
import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            final @NonNull @DataDirectory Path directory
    ) {
        this.velocityInjector = velocityInjector;
        this.server = server;
        this.directory = directory;
    }

    @Subscribe
    public void onInitialisation(final @NonNull ProxyInitializeEvent event) throws IOException {
        Files.createDirectories(this.directory);

        Injector injector = this.velocityInjector.createChildInjector(new VelocityModule(this, this.server, this.directory));

        this.pureTickets = injector.getInstance(PureTickets.class);
        this.pureTickets.load();

        try {
            CommandManager<OnlineSoul> commandManager = this.commandManager(injector);
            this.pureTickets.commands(commandManager, COMMANDS);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        for (final Class<? extends VelocitySubscriber> listenerClass : LISTENERS) {
            VelocitySubscriber listener = injector.getInstance(listenerClass);
            this.server.getEventManager().register(listener, this);
        }
    }

    @Subscribe
    public void onShutdown(final @NonNull ProxyShutdownEvent event) {
        this.pureTickets.unload();
    }

    private CommandManager<OnlineSoul> commandManager(
            final @NonNull Injector injector
    ) {
        OldMessageService oldMessageService = injector.getInstance(OldMessageService.class);
        VelocityUserService velocityUserService = (VelocityUserService) injector.getInstance(UserService.class);

        VelocityCommandManager<@NonNull OnlineSoul> cloudManager = new VelocityCommandManager<>(
                injector.getInstance(PluginContainer.class),
                injector.getInstance(ProxyServer.class),
                AsynchronousCommandExecutionCoordinator.<OnlineSoul>newBuilder().build(),
                source -> {
                    VelocityUserService userService = (VelocityUserService) injector.getInstance(UserService.class);

                    if (source instanceof Player) {
                        return userService.player((Player) source);
                    }

                    return userService.console();
                },
                soul -> {
                    VelocityOnlineSoul velocitySoul = (VelocityOnlineSoul) soul;
                    return velocitySoul.source();
                }
        );

        this.pureTickets.defaultCommandManagerSettings(cloudManager);
        return cloudManager;
    }

    @Override
    public ClassLoader loader() {
        return this.getClass().getClassLoader();
    }

}
