package co.uk.magmo.puretickets.exceptions

import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.locale.Messages

class TooManyOpenTickets : InvalidCommandArgument(Messages.EXCEPTIONS__TOO_MANY_OPEN_TICKETS, false, "%limit%", Config.limitOpenTicket.toString())