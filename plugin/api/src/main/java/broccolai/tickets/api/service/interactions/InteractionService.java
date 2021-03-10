package broccolai.tickets.api.service.interactions;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.PlayerSoul;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface InteractionService {

    Ticket create(@NonNull PlayerSoul soul, @NonNull MessageInteraction interaction);

    void pick();

}
