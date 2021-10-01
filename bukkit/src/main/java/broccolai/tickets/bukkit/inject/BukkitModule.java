package broccolai.tickets.bukkit.inject;

import broccolai.tickets.api.service.tasks.TaskService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.bukkit.service.BukkitTaskService;
import broccolai.tickets.bukkit.service.BukkitUserService;
import broccolai.tickets.core.inject.ForTickets;
import broccolai.tickets.core.inject.module.CoreModule;
import broccolai.tickets.core.inject.platform.PluginPlatform;
import com.google.inject.AbstractModule;
import java.nio.file.Path;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BukkitModule extends AbstractModule {

    private final Plugin plugin;
    private final PluginPlatform platform;

    public BukkitModule(final @NonNull Plugin plugin, final @NonNull PluginPlatform platform) {
        this.plugin = plugin;
        this.platform = platform;
    }

    @Override
    protected void configure() {
        this.install(new CoreModule());

        this.bind(ClassLoader.class).annotatedWith(ForTickets.class).toInstance(this.platform.loader());
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(Path.class).annotatedWith(ForTickets.class).toInstance(this.plugin.getDataFolder().toPath());
        this.bind(AudienceProvider.class).toInstance(BukkitAudiences.create(this.plugin));
        this.bind(TaskService.class).to(BukkitTaskService.class);
        this.bind(UserService.class).to(BukkitUserService.class);
    }

}
