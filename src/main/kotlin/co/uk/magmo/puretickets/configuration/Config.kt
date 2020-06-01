package co.uk.magmo.puretickets.configuration

import co.uk.magmo.puretickets.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.logging.Level

object Config {
    var locale = "en-US"
    var reminderDelay = 5
    var reminderRepeat = 15

    var aliasCreate = "create|c"
    var aliasUpdate = "update|u"
    var aliasClose = "close|cl"
    var aliasShow = "show|s"
    var aliasPick = "pick|p"
    var aliasAssign = "assign|a"
    var aliasDone = "done|d"
    var aliasYield = "yield|y"
    var aliasNote = "note"
    var aliasReopen = "reopen|ro"
    var aliasTeleport = "teleport|tp"
    var aliasLog = "log"
    var aliasList = "list|l"
    var aliasStatus = "status"

    fun loadFile(plugin: Plugin) {
        plugin.saveDefaultConfig()

        val target = File(plugin.dataFolder, "config.yml")
        val stream = plugin.javaClass.getResource("/config.yml").openStream()

        Utils.mergeYAML(stream, target)

        plugin.reloadConfig()

        plugin.config.run {
            locale = process("locale", locale)
            reminderDelay = process("reminder.delay", reminderDelay)
            reminderRepeat = process("reminder.repeat", reminderRepeat)

            aliasCreate = process("alias.create", aliasCreate)
            aliasUpdate = process("alias.update", aliasUpdate)
            aliasClose = process("alias.close", aliasClose)
            aliasShow = process("alias.show", aliasShow)
            aliasPick = process("alias.pick", aliasPick)
            aliasAssign = process("alias.assign", aliasAssign)
            aliasDone = process("alias.done", aliasDone)
            aliasYield = process("alias.yield", aliasYield)
            aliasNote = process("alias.note", aliasNote)
            aliasReopen = process("alias.reopen", aliasReopen)
            aliasTeleport = process("alias.teleport", aliasTeleport)
            aliasLog = process("alias.log", aliasLog)
            aliasList = process("alias.list", aliasList)
            aliasStatus = process("alias.status", aliasStatus)
        }
    }

    private inline fun <reified T> FileConfiguration.process(path: String, default: Any): T {
        var value = get(path)

        if (value == null || value !is T) {
            Bukkit.getLogger().log(Level.WARNING, path + " has a incorrect value it has been set to " + default)
            value = default
        }

        return value as T
    }
}