package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;
import co.aikar.commands.InvalidCommandArgument;

/**
 * Exception representing an invalid setting.
 */
public class InvalidSettingType extends InvalidCommandArgument {
    public InvalidSettingType() {
        super(Messages.EXCEPTIONS__INVALID_SETTING_TYPE, false);
    }
}
