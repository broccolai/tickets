package broccolai.tickets.api.service.interactions;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface InteractionService {

    Ticket create(@NonNull PlayerSoul soul, @NonNull MessageInteraction interaction);

    void update(@NonNull PlayerSoul soul, @NonNull Ticket ticket, @NonNull MessageInteraction interaction);

    void close(@NonNull PlayerSoul soul, @NonNull Ticket ticket);

    void claim(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void complete(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void assign(@NonNull OnlineSoul soul, @NonNull Soul target, @NonNull Ticket ticket);

    void unclaim(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void reopen(@NonNull OnlineSoul soul, @NonNull Ticket ticket);

    void note(@NonNull OnlineSoul soul, @NonNull Ticket ticket, @NonNull MessageInteraction message);

}
