package broccolai.tickets.core.model.interaction;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public final class BasicMessageInteraction extends MessageInteraction {

    public BasicMessageInteraction(
            final @NonNull Action action,
            final @NonNull LocalDateTime time,
            final @NonNull UUID sender,
            final @NonNull String message
    ) {
        super(action, time, sender, message);
    }

}
