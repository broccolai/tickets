package broccolai.tickets.core.inject.module;

import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.interactions.InteractionService;
import broccolai.tickets.api.service.intergrations.DiscordService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.service.event.KyoriEventService;
import broccolai.tickets.core.service.interaction.EventInteractionService;
import broccolai.tickets.core.service.intergrations.HttpDiscordService;
import broccolai.tickets.core.service.message.MiniMessageService;
import broccolai.tickets.core.service.storage.DatabaseStorageService;
import broccolai.tickets.core.service.template.MiniTemplateService;
import broccolai.tickets.core.service.ticket.CachedTicketService;
import com.google.inject.AbstractModule;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(StorageService.class).to(DatabaseStorageService.class);
        this.bind(TicketService.class).to(CachedTicketService.class);
        this.bind(TemplateService.class).to(MiniTemplateService.class);
        this.bind(MessageService.class).to(MiniMessageService.class);
        this.bind(EventService.class).to(KyoriEventService.class);
        this.bind(InteractionService.class).to(EventInteractionService.class);
        this.bind(DiscordService.class).to(HttpDiscordService.class);
    }

}
