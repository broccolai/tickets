package broccolai.tickets.core.model.user;

import broccolai.tickets.core.utilities.TicketLocation;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PlayerSoul extends OnlineSoul {
    @NonNull TicketLocation location();

    void teleport(@NonNull TicketLocation location);
}
