package broccolai.tickets.core.inject.platform;

import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.commands.command.PureTicketsCommand;
import broccolai.tickets.core.commands.command.TicketCommand;
import broccolai.tickets.core.commands.command.TicketsCommand;
import broccolai.tickets.core.utilities.ArrayHelper;

public interface PluginPlatform {

    Class<? extends BaseCommand>[] COMMANDS = ArrayHelper.create(
            TicketCommand.class,
            TicketsCommand.class,
            PureTicketsCommand.class
    );

    ClassLoader loader();

}
