package co.uk.magmo.puretickets.ticket;

import co.uk.magmo.puretickets.utilities.PureColor;

public enum TicketStatus {
    OPEN(PureColor.GREEN), PICKED(PureColor.YELLOW), CLOSED(PureColor.RED);

    private final PureColor pureColor;

    TicketStatus(PureColor pureColor) {
        this.pureColor = pureColor;
    }

    public PureColor getPureColor() {
        return pureColor;
    }

    public static TicketStatus from(String input) {
        for (TicketStatus value : values()) {
            if (value.name().equals(input)) {
                return value;
            }
        }

        return null;
    }
}