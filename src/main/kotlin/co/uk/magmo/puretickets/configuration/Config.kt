package co.uk.magmo.puretickets.configuration

import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS

object Config {
    var locale = "en-US"

    init {
        loadFile()
    }

    fun loadFile() {
        TICKETS.saveDefaultConfig()
        TICKETS.reloadConfig()

        val pluginConfig = TICKETS.config
        locale = pluginConfig.getString("locale", locale) ?: locale
    }
}