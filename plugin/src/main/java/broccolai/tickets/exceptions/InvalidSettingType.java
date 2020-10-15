package broccolai.tickets.exceptions;

import broccolai.tickets.locale.Messages;

/**
 * Exception representing an invalid setting
 */
public final class InvalidSettingType extends PureException {

    /**
     * Exception for when an invalid setting type is entered
     */
    public InvalidSettingType() {
        super(Messages.EXCEPTIONS__INVALID_SETTING_TYPE);
    }

}
