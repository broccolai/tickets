package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.impl.SoulJoinEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import broccolai.tickets.core.utilities.Constants;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.EnumSet;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SoulSubscriber implements Subscriber {

    private final StorageService storageService;
    private final TicketService ticketService;
    private final MessageService messageService;

    @Inject
    public SoulSubscriber(
            final @NonNull StorageService storageService,
            final @NonNull TicketService ticketService,
            final @NonNull MessageService messageService
    ) {
        this.storageService = storageService;
        this.ticketService = ticketService;
        this.messageService = messageService;
    }

    @Override
    public void register(final @NonNull EventService eventService) {
        eventService.register(SoulJoinEvent.class, this::onSoulJoin);
    }

    public void onSoulJoin(final @NonNull SoulJoinEvent event) {
        PlayerSoul soul = event.soul();

        Collection<Component> notifications = this.storageService.notifications(soul);

        for (final Component notification : notifications) {
            soul.sendMessage(notification);
        }

        if (soul.permission(Constants.STAFF_PERMISSION + ".announce")) {
            Multimap<UUID, Ticket> tickets = this.ticketService.get(EnumSet.of(TicketStatus.OPEN, TicketStatus.CLAIMED));
            int total = tickets.entries().size();

            if (total != 0) {
                this.messageService.taskReminder(soul, total);
            }
        }
    }

}
