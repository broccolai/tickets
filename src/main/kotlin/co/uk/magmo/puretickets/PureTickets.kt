package co.uk.magmo.puretickets

import co.aikar.idb.DB
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.integrations.DiscordManager
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.user.UserManager
import org.bukkit.plugin.java.JavaPlugin

class PureTickets : JavaPlugin() {
    private lateinit var taskManager: TaskManager
    private lateinit var notificationManager: NotificationManager

    override fun onEnable() {
        Config.loadFile(this)
        SQLFunctions.setup(this)

        val userManager = UserManager()
        val discordManager = DiscordManager()
        commandManager = CommandManager(this)
        val ticketManager = TicketManager()

        taskManager = TaskManager(this)
        notificationManager = NotificationManager(userManager, commandManager, discordManager, ticketManager, taskManager)

        commandManager.registerCompletions(ticketManager)
        commandManager.registerInjections(userManager, ticketManager, notificationManager, taskManager)
        commandManager.registerCommands()

        server.pluginManager.registerEvents(notificationManager, this)
    }

    override fun onDisable() {
        taskManager.clear()
        notificationManager.save()

        DB.close()
    }

    // TODO: Write my own locale manager
    companion object {
        lateinit var commandManager: CommandManager
    }
}