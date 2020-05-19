package co.uk.magmo.puretickets

import co.aikar.idb.DB
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.plugin.java.JavaPlugin

class PureTickets : JavaPlugin() {
    lateinit var taskManager: TaskManager
    lateinit var ticketManager: TicketManager
    lateinit var notificationManager: NotificationManager

    override fun onEnable() {
        Config.loadFile(this)
        SQLFunctions.setup(this)

        commandManager = CommandManager(this)

        ticketManager = TicketManager()
        notificationManager = NotificationManager(commandManager)

        commandManager.registerCompletions(ticketManager)
        commandManager.registerInjections(ticketManager, notificationManager)
        commandManager.registerCommands()

        taskManager = TaskManager(this, ticketManager, notificationManager)

        server.pluginManager.registerEvents(notificationManager, this)
    }

    override fun onDisable() {
        taskManager.clear()

        DB.close()
    }

    companion object {
        lateinit var commandManager: CommandManager
    }
}