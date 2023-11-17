package love.broccolai.tickets.minecraft.paper.inject;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.FilteringCommandSuggestionProcessor;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.nio.file.Path;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.paper.model.PaperConsoleCommander;
import org.bukkit.plugin.Plugin;
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
    }

    @Provides
    @Singleton
    public CommandManager<Commander> provideCommandManager(
        final Plugin plugin
    ) throws Exception {
        PaperCommandManager<Commander> commandManager = new PaperCommandManager<>(
            plugin,
            AsynchronousCommandExecutionCoordinator.<Commander>builder().withAsynchronousParsing().build(),
            PaperConsoleCommander::of,
            PaperConsoleCommander::sender
        );

        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier();
        }

        commandManager.commandSuggestionProcessor(
            new FilteringCommandSuggestionProcessor<>(
                FilteringCommandSuggestionProcessor.Filter.<Commander>contains(true).andTrimBeforeLastSpace()
            )
        );

        return commandManager;
    }

}
