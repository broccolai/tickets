package co.uk.magmo.puretickets.exceptions;

import co.aikar.commands.InvalidCommandArgument;
import co.uk.magmo.puretickets.locale.Messages;

public class InvalidSettingType extends InvalidCommandArgument {
    public InvalidSettingType() {
        super(Messages.EXCEPTIONS__INVALID_SETTING_TYPE, false);
    }
}
