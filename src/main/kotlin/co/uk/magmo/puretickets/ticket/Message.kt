package co.uk.magmo.puretickets.ticket

import org.bukkit.Bukkit
import java.time.LocalDateTime
import java.util.UUID

class Message(val reason: MessageReason, val data: String?, val sender: UUID?, val date: LocalDateTime? = LocalDateTime.now()) {
    fun displayName() = if (sender == null) Bukkit.getConsoleSender().name else Bukkit.getOfflinePlayer(sender).name
}