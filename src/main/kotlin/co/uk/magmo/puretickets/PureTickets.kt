package co.uk.magmo.puretickets

import co.aikar.idb.DB
import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.integrations.DiscordManager
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.storage.MySQLManager
import co.uk.magmo.puretickets.storage.SQLiteManager
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.user.UserManager
import org.bukkit.plugin.java.JavaPlugin

class PureTickets : JavaPlugin() {
    private lateinit var taskManager: TaskManager
    private lateinit var notificationManager: NotificationManager

    override fun onEnable() {
        Config.loadFile(this)

        val sqlManager = if (Config.storageMySQL) {
            MySQLManager()
        } else {
            SQLiteManager()
        }

        sqlManager.setup(this)

        val userManager = UserManager(sqlManager)
        val discordManager = DiscordManager()
        val commandManager = CommandManager(this)
        val ticketManager = TicketManager(sqlManager)

        taskManager = TaskManager(this)
        notificationManager = NotificationManager(userManager, commandManager, discordManager, sqlManager, ticketManager, taskManager)

        commandManager.registerCompletions(ticketManager, sqlManager)
        commandManager.registerInjections(userManager, ticketManager, notificationManager, taskManager, sqlManager)
        commandManager.registerCommands()

        server.pluginManager.registerEvents(notificationManager, this)
    }

    override fun onDisable() {
        if (::taskManager.isInitialized)
            taskManager.clear()

        if (::notificationManager.isInitialized)
            notificationManager.save()

        DB.close()
    }
}