package broccolai.tickets.core.factory;

import broccolai.tickets.core.commands.arguments.TargetArgument;
import broccolai.tickets.core.commands.arguments.TicketArgument;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CloudArgumentFactory {

    @NonNull TargetArgument target(
            @Assisted("name") @NonNull String name
    );

    @NonNull TicketArgument ticket(
            @Assisted("name") @NonNull String name
    );

}
