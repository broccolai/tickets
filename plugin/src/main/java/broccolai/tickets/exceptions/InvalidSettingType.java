package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

/**
 * Exception representing an invalid setting.
 */
public class InvalidSettingType extends PureException {
    public InvalidSettingType() {
        super(Messages.EXCEPTIONS__INVALID_SETTING_TYPE);
    }
}
