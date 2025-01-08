package love.broccolai.tickets.minecraft.paper.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import java.nio.file.Path;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import love.broccolai.tickets.minecraft.paper.PaperUUIDUsernameConverter;
import love.broccolai.tickets.minecraft.paper.model.PaperSenderMapper;
import love.broccolai.tickets.minecraft.paper.parsers.PaperLocationDescriptor;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.brigadier.CloudBrigadierManager;
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

        this.bind(LocationDescriptor.class).to(PaperLocationDescriptor.class);
        this.bind(UUIDUsernameConverter.class).to(PaperUUIDUsernameConverter.class);
    }

    @Provides
    @Singleton
    public CommandManager<Commander> provideCommandManager(
        final Plugin plugin
    ) {
        PaperCommandManager<Commander> commandManager = PaperCommandManager.builder(new PaperSenderMapper())
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(plugin);

        commandManager.suggestionProcessor(
            new FilteringSuggestionProcessor<>(
                FilteringSuggestionProcessor.Filter.contains(true)
            )
        );

        CloudBrigadierManager<Commander, ? extends CommandSourceStack> brigadierManager = commandManager.brigadierManager();

        brigadierManager.registerMapping(TypeToken.get(PaperLocationDescriptor.class), builder -> {
            builder.to(argument -> ArgumentTypes.blockPosition());
        });

        return commandManager;
    }

}
