package co.uk.magmo.puretickets.exceptions

import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.locale.Messages

class TicketNotFound : InvalidCommandArgument(Messages.EXCEPTIONS__TICKET_NOT_FOUND, false)