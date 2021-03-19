package broccolai.tickets.core.factory;

import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.core.commands.arguments.TargetArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import broccolai.tickets.core.commands.arguments.TicketParserMode;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

public interface CloudArgumentFactory {

    @NonNull TargetArgument target(
            @Assisted("name") @NonNull String name
    );

    @NonNull TicketArgument ticket(
            @Assisted("name") @NonNull String name,
            @Assisted("mode") @NonNull TicketParserMode mode,
            @Assisted("suggestions") @NonNull Set<TicketStatus> suggestions,
            @Assisted("padding") int padding
    );

}
