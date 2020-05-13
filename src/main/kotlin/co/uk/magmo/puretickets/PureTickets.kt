package co.uk.magmo.puretickets

import co.aikar.idb.DB
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.interactions.Notifications
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.TaskManager
import org.bukkit.plugin.java.JavaPlugin

class PureTickets : JavaPlugin() {
    var taskManager: TaskManager? = null

    override fun onEnable() {
        TICKETS = this
        commandManager = CommandManager()

        SQLFunctions.setup()
        server.pluginManager.registerEvents(Notifications, this)

        taskManager = TaskManager()
    }

    override fun onDisable() {
        taskManager?.clear()

        DB.close()
    }

    companion object {
        lateinit var TICKETS: PureTickets
        lateinit var commandManager: CommandManager
    }
}