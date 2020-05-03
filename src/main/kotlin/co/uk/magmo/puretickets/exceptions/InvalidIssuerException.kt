package co.uk.magmo.puretickets.exceptions

import co.aikar.commands.InvalidCommandArgument

class InvalidIssuerException : InvalidCommandArgument("Must be a player", false)