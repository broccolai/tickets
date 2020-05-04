package co.uk.magmo.puretickets.utils

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

fun UUID.asPlayer() = Bukkit.getOfflinePlayer(this)

fun UUID?.asName() = if (this == null) Bukkit.getConsoleSender().name else Bukkit.getOfflinePlayer(this).name

fun CommandSender.asUUID() = if (this is Player) this.uniqueId else null;