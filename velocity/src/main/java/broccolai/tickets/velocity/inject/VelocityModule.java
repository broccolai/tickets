package broccolai.tickets.velocity.inject;

import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.inject.ForTickets;
import broccolai.tickets.core.inject.module.CoreModule;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import broccolai.tickets.velocity.service.VelocityTaskService;
import broccolai.tickets.velocity.service.VelocityUserService;
import com.google.inject.AbstractModule;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VelocityModule extends AbstractModule {

    private final ProxyServer server;
    private final PluginPlatform platform;
    private final Path folder;

    public VelocityModule(
            final @NonNull PluginPlatform platform,
            final @NonNull ProxyServer server,
            final @NonNull Path folder
    ) {
        this.server = server;
        this.platform = platform;
        this.folder = folder;
    }

    @Override
    protected void configure() {
        this.install(new CoreModule());

        this.bind(ClassLoader.class).annotatedWith(ForTickets.class).toInstance(this.platform.loader());
        this.bind(ProxyServer.class).toInstance(this.server);
        this.bind(Path.class).annotatedWith(ForTickets.class).toInstance(this.folder);
        this.bind(TaskService.class).to(VelocityTaskService.class);
        this.bind(UserService.class).to(VelocityUserService.class);
    }

}
