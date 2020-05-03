package co.uk.magmo.puretickets

import co.uk.magmo.puretickets.commands.CommandManager
import co.uk.magmo.puretickets.interactions.Notifications
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.plugin.java.JavaPlugin

class PureTickets : JavaPlugin() {
    override fun onEnable() {
        TICKETS = this
        commandManager = CommandManager()

        SQLFunctions.setup()
        server.pluginManager.registerEvents(Notifications, this)
    }

    companion object {
        lateinit var TICKETS: PureTickets
        lateinit var commandManager: CommandManager
    }
}