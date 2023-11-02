package broccolai.tickets.paper.inject;

import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.paper.service.PaperTaskService;
import broccolai.tickets.paper.service.PaperUserService;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import com.google.inject.AbstractModule;

import java.io.File;

import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PaperModule extends AbstractModule {

    private final Plugin plugin;
    private final PluginPlatform platform;

    public PaperModule(final @NonNull Plugin plugin, final @NonNull PluginPlatform platform) {
        this.plugin = plugin;
        this.platform = platform;
    }

    @Override
    protected void configure() {
        this.bind(ClassLoader.class).toInstance(this.platform.loader());
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(File.class).toInstance(this.plugin.getDataFolder());
        this.bind(TaskService.class).to(PaperTaskService.class);
        this.bind(UserService.class).to(PaperUserService.class);
    }

}
