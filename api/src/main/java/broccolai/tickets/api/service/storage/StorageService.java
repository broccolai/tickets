package broccolai.tickets.api.service.storage;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.service.Disposable;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.Ticket.Status;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.model.user.UserSnapshot;
import com.google.common.collect.Multimap;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface StorageService extends Disposable {

    @NonNull Ticket create(@NonNull User user, @NonNull MessageInteraction messageInteraction);

    @NonNull Collection<@NonNull Ticket> tickets(@NonNull Collection<@NonNull Integer> ids);

    @NonNull Collection<@NonNull Ticket> findTickets(@NonNull Collection<Status> statuses);

    @NonNull Collection<@NonNull Ticket> findTickets(
            @NonNull User user,
            @NonNull Collection<@NonNull Status> statuses
    );

    void updateTickets(@NonNull Collection<Ticket> tickets);

    void saveInteractions(@NonNull Multimap<Ticket, Interaction> interactions);

    @NonNull Collection<@NonNull Interaction> interactions(@NonNull Ticket ticket);

    @NonNull Collection<@NonNull Component> notifications(@NonNull User user);

    void saveNotification(@NonNull User user, @NonNull Component component);

    @NonNull Map<@NonNull UUID, @NonNull Integer> highscores(@NonNull ChronoUnit chronoUnit);

    void saveSnapshots(@NonNull Collection<@NonNull UserSnapshot> snapshots);

    @Nullable UserSnapshot snapshot(@NonNull String name);

    @Nullable UserSnapshot snapshot(@NonNull UUID uuid);

    void queue(@NonNull Ticket ticket, @NonNull Interaction interaction);

    void clear();

}
