package co.uk.magmo.puretickets.utils

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun UUID?.asName() = if (this == null) Bukkit.getConsoleSender().name else Bukkit.getOfflinePlayer(this).name!!

fun CommandSender.asUUID() = if (this is Player) this.uniqueId else null

fun Int.minuteToTick() = (this * 60 * 20).toLong()

fun LocalDateTime?.formatted() = this?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))!!