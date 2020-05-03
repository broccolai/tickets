package co.uk.magmo.puretickets.locale

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider {
    TICKET_CREATED, TICKET_PICKED, TICKET_CLOSED, TICKET_DONE;

    override fun getMessageKey(): MessageKey {
        return MessageKey.of(name.toLowerCase().replace("_", "."))
    }
}