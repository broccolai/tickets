package co.uk.magmo.puretickets.locale

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider {
    TASKS__REMINDER,
    TICKET__CREATED, TICKET__UPDATED, TICKET__CLOSED, TICKET__PICKED, TICKET__YIELDED, TICKET__DONE, TICKET__REOPENED,
    NOTIFICATIONS__PICK, NOTIFICATIONS__YIELD, NOTIFICATIONS__DONE, NOTIFICATIONS__REOPEN,
    ANNOUNCEMENTS__NEW_TICKET,
    TITLES__ALL_TICKETS, TITLES__SHOW_TICKET, TITLES__TICKET_LOG;

    override fun getMessageKey(): MessageKey {
        return MessageKey.of(name.toLowerCase().replace("__", "."))
    }
}