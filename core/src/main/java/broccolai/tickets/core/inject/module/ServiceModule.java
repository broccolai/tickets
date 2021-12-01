package broccolai.tickets.core.inject.module;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.service.context.ContextService;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.modification.ModificationService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import broccolai.tickets.core.service.context.MappedContextService;
import broccolai.tickets.core.service.event.KyoriEventService;
import broccolai.tickets.core.service.interaction.EventModificationService;
import broccolai.tickets.core.service.intergrations.HttpDiscordService;
import broccolai.tickets.core.service.message.BasicReceiverResolver;
import broccolai.tickets.core.service.message.InteractionPlaceholderResolver;
import broccolai.tickets.core.service.message.MessageRenderer;
import broccolai.tickets.core.service.message.NumberPlaceholderResolver;
import broccolai.tickets.core.service.message.SoulPlaceholderResolver;
import broccolai.tickets.core.service.message.PermissionReceiverResolver;
import broccolai.tickets.core.service.message.StringPlaceholderResolver;
import broccolai.tickets.core.service.message.TicketPlaceholderResolver;
import broccolai.tickets.core.service.storage.DatabaseStorageService;
import broccolai.tickets.core.service.template.MiniTemplateService;
import broccolai.tickets.core.service.ticket.StorageTicketService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.Moonshine;
import net.kyori.moonshine.exception.scan.UnscannableMethodException;
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy;
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(StorageService.class).to(DatabaseStorageService.class);
        this.bind(TicketService.class).to(StorageTicketService.class);
        this.bind(TemplateService.class).to(MiniTemplateService.class);
        this.bind(EventService.class).to(KyoriEventService.class);
        this.bind(ModificationService.class).to(EventModificationService.class);
        this.bind(DiscordService.class).to(HttpDiscordService.class);
        this.bind(ContextService.class).to(MappedContextService.class);
    }

    @Provides
    public MessageService messageService(
            final @NonNull LocaleConfiguration localeConfiguration,
            final @NonNull BasicReceiverResolver basicReceiverResolver,
            final @NonNull PermissionReceiverResolver permissionReceiverResolver,
            final @NonNull MessageRenderer messageRenderer,
            final @NonNull StringPlaceholderResolver stringPlaceholderResolver,
            final @NonNull NumberPlaceholderResolver numberPlaceholderResolver,
            final @NonNull TicketPlaceholderResolver ticketPlaceholderResolver,
            final @NonNull InteractionPlaceholderResolver interactionPlaceholderResolver,
            final @NonNull SoulPlaceholderResolver soulPlaceholderResolver
    ) throws UnscannableMethodException {
        return Moonshine.<MessageService, Audience>builder(TypeToken.get(MessageService.class))
                .receiverLocatorResolver(basicReceiverResolver, 0)
                .receiverLocatorResolver(permissionReceiverResolver, 1)
                .sourced((audience, key) -> localeConfiguration.get(key))
                .rendered(messageRenderer)
                .sent(Audience::sendMessage)
                .resolvingWithStrategy(
                        new StandardPlaceholderResolverStrategy<>(new StandardSupertypeThenInterfaceSupertypeStrategy(true))
                )
                .weightedPlaceholderResolver(String.class, stringPlaceholderResolver, 1)
                .weightedPlaceholderResolver(Number.class, numberPlaceholderResolver, 1)
                .weightedPlaceholderResolver(Ticket.class, ticketPlaceholderResolver, 1)
                .weightedPlaceholderResolver(Interaction.class, interactionPlaceholderResolver, 1)
                .weightedPlaceholderResolver(User.class, soulPlaceholderResolver, 1)
                .create(this.getClass().getClassLoader());
    }

}
