package co.uk.magmo.puretickets.storage

import co.aikar.idb.DbRow
import co.uk.magmo.puretickets.interactions.PendingNotification
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.user.UserSettings
import com.google.common.collect.ArrayListMultimap
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import java.sql.SQLException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

interface SQLManager {
    fun setup(plugin: Plugin)

    val ticket: TicketFunctions

    val message: MessageFunctions

    val notification: NotificationFunctions

    val settings: SettingsFunctions

    interface TicketFunctions {
        fun select(id: Int): Ticket

        fun selectAll(status: TicketStatus? = null): List<Ticket>

        fun selectAll(uuid: UUID, status: TicketStatus? = null): List<Ticket>

        fun selectIds(uuid: UUID, status: TicketStatus? = null): List<Int>

        fun selectHighestId(uuid: UUID, isActive: Boolean): Int?

        fun selectNames(status: TicketStatus? = null): List<String>

        fun selectTicketStats(uuid: UUID? = null): HashMap<TicketStatus, Int>

        fun exists(id: Int): Boolean

        fun count(status: TicketStatus? = null): Int

        fun insert(uuid: UUID, status: TicketStatus, picker: UUID?, location: Location): Int

        fun update(ticket: Ticket)
    }

    interface MessageFunctions {
        fun selectAll(id: Int): ArrayList<Message>

        fun insert(ticket: Ticket, message: Message)
    }

    interface NotificationFunctions {
        fun selectAllAndClear(): ArrayListMultimap<UUID, PendingNotification>

        fun insertAll(notifications: ArrayListMultimap<UUID, PendingNotification>)
    }

    interface SettingsFunctions {
        fun select(uuid: UUID): UserSettings

        fun exists(uuid: UUID): Boolean

        fun insert(uuid: UUID, settings: UserSettings)

        fun update(uuid: UUID, settings: UserSettings)
    }

    // Helper Functions

    fun DbRow.getUUID(column: String): UUID? {
        val raw = getString(column)

        return if (raw == "null" || raw == null) null else UUID.fromString(raw)
    }

    fun DbRow.getLocation(column: String): Location {
        val split = getString(column)?.split("|")!!
        val world = Bukkit.getWorld(split[0])

        return Location(world, split[1].toDouble(), split[2].toDouble(), split[3].toDouble())
    }

    fun Location.serialized(): String {
        return world?.name + "|" + blockX + "|" + blockY + "|" + blockZ
    }

    fun DbRow.getPureLong(column: String): Long {
        return getLong(column)
    }

    fun DbRow.getDate(column: String): LocalDateTime {
        val instant = Instant.ofEpochMilli(getPureLong("date"))

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun LocalDateTime.serialized(): Long {
        return atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    fun <T: Enum<T>> DbRow.getEnumValue(enumClass: Class<T>, column: String): T {
        val enumValues = enumClass.enumConstants as Array<T>
        val search = getString(column)

        return enumValues.first { it.name == search }
    }

    fun DbRow.buildTicket(): Ticket {
        val id = getInt("id")
        val player = getUUID("uuid") ?: throw SQLException()
        val messages = message.selectAll(id)
        val status = getEnumValue(TicketStatus::class.java, "status")
        val picker = getUUID("picker")
        val location = getLocation("location")

        return Ticket(id, player, messages, status, picker, location)
    }

    fun DbRow.buildMessage(): Message {
        val reason = getEnumValue(MessageReason::class.java, "reason")
        val data = getString("data")
        val sender = getUUID("sender")
        val date = getDate("date")

        return Message(reason, data, sender, date)
    }

    fun DbRow.buildNotification(): PendingNotification {
        val message = getEnumValue(Messages::class.java, "message")
        val replacements = getString("replacements").split("|").toTypedArray()

        return PendingNotification(message, *replacements)
    }
}
