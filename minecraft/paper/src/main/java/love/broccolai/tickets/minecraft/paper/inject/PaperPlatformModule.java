package love.broccolai.tickets.minecraft.paper.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import java.nio.file.Path;
import love.broccolai.tickets.common.service.profile.UUIDUsernameConverter;
import love.broccolai.tickets.minecraft.common.exceptions.InvalidProfileException;
import love.broccolai.tickets.minecraft.common.exceptions.InvalidTicketException;
import love.broccolai.tickets.minecraft.common.exceptions.ProfileNotFoundException;
import love.broccolai.tickets.minecraft.common.exceptions.TicketNotFoundException;
import love.broccolai.tickets.minecraft.common.exceptions.TicketTypeNotFoundException;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import love.broccolai.tickets.minecraft.common.service.MessageService;
import love.broccolai.tickets.minecraft.common.service.OnlineProfileProvider;
import love.broccolai.tickets.minecraft.common.service.PermissionAudienceProvider;
import love.broccolai.tickets.minecraft.common.service.TicketNotifier;
import love.broccolai.tickets.minecraft.common.service.TicketTeleportService;
import love.broccolai.tickets.minecraft.paper.PaperOnlineProfileProvider;
import love.broccolai.tickets.minecraft.paper.PaperPermissionAudienceProvider;
import love.broccolai.tickets.minecraft.paper.PaperTicketNotifier;
import love.broccolai.tickets.minecraft.paper.PaperTicketTeleportService;
import love.broccolai.tickets.minecraft.paper.PaperUUIDUsernameConverter;
import love.broccolai.tickets.minecraft.paper.model.PaperSenderMapper;
import love.broccolai.tickets.minecraft.paper.parsers.PaperLocationDescriptor;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.brigadier.CloudBrigadierManager;
import org.incendo.cloud.exception.ArgumentParseException;
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
        this.bind(OnlineProfileProvider.class).to(PaperOnlineProfileProvider.class);
        this.bind(PermissionAudienceProvider.class).to(PaperPermissionAudienceProvider.class);
        this.bind(UUIDUsernameConverter.class).to(PaperUUIDUsernameConverter.class);
        this.bind(TicketNotifier.class).to(PaperTicketNotifier.class);
        this.bind(TicketTeleportService.class).to(PaperTicketTeleportService.class);
    }

    @Provides
    @Singleton
    public CommandManager<Commander> provideCommandManager(
        final Plugin plugin,
        final MessageService messages
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

        this.registerExceptionHandlers(commandManager, messages);

        return commandManager;
    }

    private void registerExceptionHandlers(
        final CommandManager<Commander> commandManager,
        final MessageService messages
    ) {
        commandManager.exceptionController().registerHandler(ArgumentParseException.class, context -> {
            Commander commander = context.context().sender();
            Throwable cause = context.exception().getCause();

            if (cause instanceof InvalidTicketException) {
                messages.feedbackErrorInvalidTicket(commander);
            } else if (cause instanceof TicketNotFoundException) {
                messages.feedbackErrorTicketNotFound(commander);
            } else if (cause instanceof InvalidProfileException) {
                messages.feedbackErrorInvalidProfile(commander);
            } else if (cause instanceof ProfileNotFoundException) {
                messages.feedbackErrorProfileNotFound(commander);
            } else if (cause instanceof TicketTypeNotFoundException) {
                messages.feedbackErrorTicketTypeNotFound(commander);
            } else {
                throw context.exception();
            }
        });
    }
}
