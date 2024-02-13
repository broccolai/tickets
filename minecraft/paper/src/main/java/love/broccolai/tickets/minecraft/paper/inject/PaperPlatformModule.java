package love.broccolai.tickets.minecraft.paper.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.nio.file.Path;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.paper.model.PaperConsoleCommander;
import love.broccolai.tickets.minecraft.paper.service.PaperProfileService;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.suggestion.FilteringSuggestionProcessor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperPlatformModule extends AbstractModule {

    private final Plugin plugin;

    public PaperPlatformModule(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(Path.class).toInstance(this.plugin.getDataFolder().toPath());

        this.bind(ProfileService.class).to(PaperProfileService.class);
    }

    @Provides
    @Singleton
    public CommandManager<Commander> provideCommandManager(
        final Plugin plugin
    ) {
        PaperCommandManager<Commander> commandManager = new PaperCommandManager<>(
            plugin,
            ExecutionCoordinator.asyncCoordinator(),
            SenderMapper.create(
                PaperConsoleCommander::of,
                PaperConsoleCommander::sender
            )
        );

        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier();
        }

        commandManager.suggestionProcessor(
            new FilteringSuggestionProcessor<>(
                FilteringSuggestionProcessor.Filter.contains(true)
            )
        );

        return commandManager;
    }

}
