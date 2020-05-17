package co.uk.magmo.puretickets.configuration

import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.utils.Utils
import java.io.File

object Config {
    var locale = "en-US"
    var reminderDelay = 5
    var reminderRepeat = 15

    var aliasCreate = "create|c"
    var aliasUpdate = "update|u"
    var aliasClose = "close|cl"
    var aliasShow = "show|s"
    var aliasPick = "pick|p"
    var aliasDone = "done|d"
    var aliasYield = "yield|y"
    var aliasReopen = "reopen|ro"
    var aliasTeleport = "teleport|tp"
    var aliasLog = "log"
    var aliasList = "list|l"
    var aliasStatus = "status"

    fun loadFile() {
        TICKETS.saveDefaultConfig()

        val target = File(TICKETS.dataFolder, "config.yml")
        val stream = TICKETS.javaClass.getResource("/config.yml").openStream()

        Utils.mergeYAML(stream, target)

        TICKETS.reloadConfig()

        val pluginConfig = TICKETS.config

        locale = pluginConfig.getString("locale", locale) ?: locale
        reminderDelay = pluginConfig.getInt("reminder.delay", reminderDelay)
        reminderRepeat = pluginConfig.getInt("reminder.repeat", reminderRepeat)

        aliasCreate = pluginConfig.getString("alias.create", aliasCreate) ?: aliasCreate
        aliasUpdate = pluginConfig.getString("alias.update", aliasUpdate) ?: aliasUpdate
        aliasClose = pluginConfig.getString("alias.close", aliasClose) ?: aliasClose
        aliasShow = pluginConfig.getString("alias.show", aliasShow) ?: aliasShow
        aliasPick = pluginConfig.getString("alias.pick", aliasPick) ?: aliasPick
        aliasDone = pluginConfig.getString("alias.done", aliasDone) ?: aliasDone
        aliasYield = pluginConfig.getString("alias.yield", aliasYield) ?: aliasYield
        aliasReopen = pluginConfig.getString("alias.reopen", aliasReopen) ?: aliasReopen
        aliasTeleport = pluginConfig.getString("alias.teleport", aliasTeleport) ?: aliasTeleport
        aliasLog = pluginConfig.getString("alias.log", aliasLog) ?: aliasLog
        aliasList = pluginConfig.getString("alias.list", aliasList) ?: aliasList
        aliasStatus = pluginConfig.getString("alias.status", aliasStatus) ?: aliasStatus
    }
}