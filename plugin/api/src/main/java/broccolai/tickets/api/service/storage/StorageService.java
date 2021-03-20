package broccolai.tickets.api.service.storage;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.service.Disposable;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface StorageService extends Disposable {

    int create(@NonNull Soul soul, @NonNull Position position, @NonNull MessageInteraction messageInteraction);

    @NonNull Map<Integer, Ticket> tickets(@NonNull Collection<@NonNull Integer> ids);

    @NonNull Map<Integer, Ticket> findTickets(@NonNull Collection<TicketStatus> statuses);

    @NonNull Map<Integer, Ticket> findTickets(@NonNull Soul soul, @NonNull Collection<TicketStatus> statuses);

    void updateTickets(@NonNull Collection<Ticket> tickets);

    int countTickets(@NonNull Collection<TicketStatus> statuses);

    void saveInteractions(@NonNull Multimap<Ticket, Interaction> interactions);

    Collection<Interaction> interactions(@NonNull Ticket ticket);

    @NonNull Collection<Component> notifications(@NonNull Soul soul);

    void saveNotification(@NonNull Soul soul, @NonNull Component component);

    Map<UUID, Integer> highscores(@NonNull ChronoUnit chronoUnit);

    void queue(@NonNull Ticket ticket, @NonNull Interaction interaction);

    void clear();

}
