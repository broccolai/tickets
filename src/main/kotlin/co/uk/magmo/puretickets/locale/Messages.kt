package co.uk.magmo.puretickets.locale

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider {
    TICKET__CREATED, TICKET__PICKED, TICKET__CLOSED, TICKET__DONE, TITLES__ALL_TICKETS, TITLES__SHOW_TICKET;

    override fun getMessageKey(): MessageKey {
        return MessageKey.of(name.toLowerCase().replace("__", "."))
    }
}