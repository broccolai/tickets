package co.uk.magmo.puretickets.utils

import org.bukkit.Bukkit
import java.util.UUID

fun UUID.asPlayer() = Bukkit.getOfflinePlayer(this)

fun UUID?.asName() = if (this == null) Bukkit.getConsoleSender().name else Bukkit.getOfflinePlayer(this).name