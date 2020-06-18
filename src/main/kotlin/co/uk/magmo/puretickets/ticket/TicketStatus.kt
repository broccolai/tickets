package co.uk.magmo.puretickets.ticket

import co.uk.magmo.puretickets.utils.PureColors

enum class TicketStatus(val color: PureColors) {
    OPEN(PureColors.GREEN), PICKED(PureColors.YELLOW), CLOSED(PureColors.RED);

    companion object {
        fun from(input: String?): TicketStatus? {
            return values().firstOrNull { it.name == input }
        }
    }
}