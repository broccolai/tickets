package love.broccolai.tickets.minecraft.paper.inject;

import com.google.common.base.Suppliers;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.nio.file.Path;
import java.util.function.Supplier;
import com.mojang.brigadier.arguments.ArgumentType;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import love.broccolai.tickets.minecraft.paper.model.PaperPlayerCommander;
import love.broccolai.tickets.minecraft.paper.model.PaperSenderMapper;
import love.broccolai.tickets.minecraft.paper.parsers.PaperLocationDescriptor;
import love.broccolai.tickets.minecraft.paper.service.PaperProfileService;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.brigadier.CloudBrigadierManager;
import org.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import org.incendo.cloud.bukkit.parser.location.LocationParser;
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
        this.bind(LocationDescriptor.class).to(PaperLocationDescriptor.class);
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

        tryRegisterLocationBrigMapper(commandManager.brigadierManager());

        return commandManager;
    }

    //todo: there has gotta be a better way to do this
    private void tryRegisterLocationBrigMapper(final CloudBrigadierManager<Commander, ? extends CommandSourceStack> brigadierManager) {
        final Supplier<Class<? extends ArgumentType<?>>> argumentTypeClass = Suppliers.memoize(() -> {
            try {
                return MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("vec3"));
            } catch (final Exception e) {
                throw new RuntimeException("Failed to locate class for vec3", e);
            }
        });

        brigadierManager.registerMapping(new TypeToken<PaperLocationDescriptor>() {{}}, builder -> {
            builder.to(argument -> {
                try {
                    return argumentTypeClass.get().getDeclaredConstructor(boolean.class).newInstance(true);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

}
