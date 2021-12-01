package broccolai.tickets.api.service.modification;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineUser;
import broccolai.tickets.api.model.user.PlayerUser;
import broccolai.tickets.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ModificationService {

    @NonNull Ticket create(@NonNull PlayerUser soul, @NonNull MessageInteraction interaction);

    void update(@NonNull PlayerUser soul, @NonNull Ticket ticket, @NonNull MessageInteraction interaction);

    void close(@NonNull PlayerUser soul, @NonNull Ticket ticket);

    void claim(@NonNull OnlineUser soul, @NonNull Ticket ticket);

    void complete(@NonNull OnlineUser soul, @NonNull Ticket ticket);

    void assign(@NonNull OnlineUser soul, @NonNull User target, @NonNull Ticket ticket);

    void unclaim(@NonNull OnlineUser soul, @NonNull Ticket ticket);

    void reopen(@NonNull OnlineUser soul, @NonNull Ticket ticket);

    void note(@NonNull OnlineUser soul, @NonNull Ticket ticket, @NonNull MessageInteraction message);

}
