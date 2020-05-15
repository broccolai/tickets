package co.uk.magmo.puretickets.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

fun UUID?.asName() = if (this == null) Bukkit.getConsoleSender().name else Bukkit.getOfflinePlayer(this).name!!

fun CommandSender.asUUID() = if (this is Player) this.uniqueId else null;

fun ChatColor.bold() = "$this${ChatColor.BOLD}"

fun Int.minuteToTick() = (this * 60 * 20).toLong()

fun LocalDateTime?.formatted() = this?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))!!