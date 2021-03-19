package broccolai.tickets.core.model.interaction;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.Interaction;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public final class BasicInteraction extends Interaction {

    public BasicInteraction(
            final @NonNull Action action,
            final @NonNull LocalDateTime time,
            final @NonNull UUID sender
    ) {
        super(action, time, sender);
    }

}
