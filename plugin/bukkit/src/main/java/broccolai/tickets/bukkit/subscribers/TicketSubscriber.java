package broccolai.tickets.bukkit.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.impl.TicketCreateEvent;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.bukkit.context.BukkitTicketContextKeys;
import broccolai.tickets.bukkit.model.BukkitPlayerSoul;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketSubscriber implements Subscriber {

    @Override
    public void register(@NonNull final EventService eventService) {
        eventService.register(TicketCreateEvent.class, this::onTicketCreation);
    }

    private void onTicketCreation(final @NonNull TicketCreateEvent event) {
        Ticket ticket = event.ticket();

        BukkitPlayerSoul soul = (BukkitPlayerSoul) event.soul();
        Player player = soul.sender();

        ticket.context().put(BukkitTicketContextKeys.LOCATION, player.getLocation());
    }

}
