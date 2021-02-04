package broccolai.tickets.core.inject.platform;

import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.commands.command.TicketCommand;
import broccolai.tickets.core.commands.command.TicketsCommand;
import broccolai.tickets.core.service.UserService;
import broccolai.tickets.core.utilities.ArrayHelper;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface TicketPlugin {

    @NonNull Class<? extends BaseCommand>[] COMMAND_CLASSES = ArrayHelper.create(
            TicketCommand.class,
            TicketsCommand.class
    );

    Class<? extends UserService<?, ?>> userServiceClass();
    
}
