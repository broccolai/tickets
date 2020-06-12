package co.uk.magmo.puretickets.utils

import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.Ticket
import co.uk.magmo.puretickets.ticket.TicketStatus
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object Utils {
    fun mergeYAML(input: InputStream, destination: File) {
        val inputYaml = YamlConfiguration.loadConfiguration(InputStreamReader(input, "UTF-8"))
        val outputYaml = YamlConfiguration.loadConfiguration(destination)

        inputYaml.getKeys(true).forEach { path -> outputYaml[path] = outputYaml[path, inputYaml[path]] }
        outputYaml.save(destination)
    }

    fun ticketReplacements(ticket: Ticket?): Array<String> {
        val results = ArrayList<String>()

        if (ticket == null) return results.toTypedArray()

        results += "%id%"
        results += ticket.id.toString()

        val message = ticket.currentMessage()!!.data!!

        results += "%ticket%"
        results += message
        results += "%message%"
        results += message

        results += "%statusColor%"
        results += ticket.status.color.chatColor.toString()

        results += "%status%"
        results += ticket.status.name

        val picker = if (ticket.status == TicketStatus.PICKED) {
            ticket.pickerUUID.asName()
        } else {
            "Unpicked"
        }

        results += "%picker%"
        results += picker

        results += "%date%"
        results += ticket.dateOpened().formatted()

        results += "%note%"
        results += ticket.messages.firstOrNull { it.reason == MessageReason.NOTE }?.data ?: ""

        return results.toTypedArray()
    }
}