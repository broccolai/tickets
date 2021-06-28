package broccolai.tickets.core.inject.module;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.context.ContextService;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.message.OldMessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.service.context.MappedContextService;
import broccolai.tickets.core.service.event.KyoriEventService;
import broccolai.tickets.core.service.interaction.EventInteractionService;
import broccolai.tickets.core.service.intergrations.HttpDiscordService;
import broccolai.tickets.core.service.message.BasicReceiverResolver;
import broccolai.tickets.core.service.message.MiniOldMessageService;
import broccolai.tickets.core.service.message.StaffReceiverResolver;
import broccolai.tickets.core.service.message.moonshine.MessageRenderer;
import broccolai.tickets.core.service.message.moonshine.MessageSender;
import broccolai.tickets.core.service.message.moonshine.MessageSource;
import broccolai.tickets.core.service.message.moonshine.TicketPlaceholderResolver;
import broccolai.tickets.core.service.storage.DatabaseStorageService;
import broccolai.tickets.core.service.template.MiniTemplateService;
import broccolai.tickets.core.service.ticket.CachedTicketService;
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
        this.bind(TicketService.class).to(CachedTicketService.class);
        this.bind(TemplateService.class).to(MiniTemplateService.class);
        this.bind(OldMessageService.class).to(MiniOldMessageService.class);
        this.bind(EventService.class).to(KyoriEventService.class);
        this.bind(InteractionService.class).to(EventInteractionService.class);
        this.bind(DiscordService.class).to(HttpDiscordService.class);
        this.bind(ContextService.class).to(MappedContextService.class);
    }

    @Provides
    public MessageService messageService(
            final @NonNull BasicReceiverResolver basicReceiverResolver,
            final @NonNull StaffReceiverResolver staffReceiverResolver,
            final @NonNull MessageSource messageSource,
            final @NonNull MessageRenderer messageRenderer,
            final @NonNull MessageSender messageSender,
            final @NonNull TicketPlaceholderResolver ticketPlaceholderResolver
    ) throws UnscannableMethodException {
        return Moonshine.<MessageService, Audience>builder(TypeToken.get(MessageService.class))
                .receiverLocatorResolver(basicReceiverResolver, 0)
                .receiverLocatorResolver(staffReceiverResolver, 1)
                .sourced(messageSource)
                .rendered(messageRenderer)
                .sent(messageSender)
                .resolvingWithStrategy(
                        new StandardPlaceholderResolverStrategy<>(new StandardSupertypeThenInterfaceSupertypeStrategy(true))
                )
                .weightedPlaceholderResolver(Ticket.class, ticketPlaceholderResolver, 0)
                .create(this.getClass().getClassLoader());
    }

}
