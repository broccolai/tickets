package broccolai.tickets.api.service.interactions;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface InteractionService {

    Ticket create(@NonNull PlayerSoul soul, @NonNull MessageInteraction interaction);

    void claim(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void queue(@NonNull Interaction interaction);

    Collection<Interaction> queued();

}
