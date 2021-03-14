package broccolai.tickets.api.service.interactions;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import com.google.common.collect.Multimap;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface InteractionService {

    Ticket create(@NonNull PlayerSoul soul, @NonNull MessageInteraction interaction);

    void close(@NonNull PlayerSoul soul, @NonNull Ticket ticket);

    void claim(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void complete(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void queue(@NonNull Ticket ticket, @NonNull Interaction interaction);

    Multimap<Integer, Interaction> queued();

}
