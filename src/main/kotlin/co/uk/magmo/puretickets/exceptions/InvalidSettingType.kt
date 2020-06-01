package co.uk.magmo.puretickets.exceptions

import co.aikar.commands.InvalidCommandArgument
import co.uk.magmo.puretickets.locale.Messages

class InvalidSettingType : InvalidCommandArgument(Messages.EXCEPTIONS__INVALID_SETTING_TYPE, false)