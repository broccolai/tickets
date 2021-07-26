package broccolai.tickets.api.model.ticket;

import broccolai.corn.context.Context;
import broccolai.tickets.api.model.interaction.Interactions;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Ticket {

    int id();

    @NonNull UUID player();

    @NonNull TicketStatus status();

    void status(@NonNull TicketStatus status);

    @NonNull Optional<@NonNull UUID> claimer();

    void claimer(@Nullable UUID claimer);

    @NonNull Context context();

    @NonNull Interactions interactions();

}
