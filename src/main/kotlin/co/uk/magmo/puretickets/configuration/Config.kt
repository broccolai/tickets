package co.uk.magmo.puretickets.configuration

import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS

object Config {
    var locale = "en-US"
    var reminderDelay = 5
    var reminderRepeat = 15

    init {
        loadFile()
    }

    fun loadFile() {
        TICKETS.saveDefaultConfig()
        TICKETS.reloadConfig()

        val pluginConfig = TICKETS.config
        locale = pluginConfig.getString("locale", locale) ?: locale
        reminderDelay = pluginConfig.getInt("reminder.delay", reminderDelay)
        reminderRepeat = pluginConfig.getInt("reminder.repeat", reminderRepeat)
    }
}