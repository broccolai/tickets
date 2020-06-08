package co.uk.magmo.puretickets.commands

import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import co.uk.magmo.puretickets.configuration.Config
import co.uk.magmo.puretickets.locale.TargetType
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.*
import co.uk.magmo.puretickets.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.*

class CommandManager(plugin: Plugin) : PaperCommandManager(plugin) {
    init {
        enableUnstableAPI("help")
        saveLocales()
        loadLocales()

        // Colours
        setFormat(MessageType.HELP, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY)
        setFormat(MessageType.INFO, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY)

        // Contexts
        commandContexts.registerContext(Message::class.java) { c ->
            Message(MessageReason.MESSAGE, c.joinArgs(), null)
        }

        commandContexts.registerContext(TicketStatus::class.java) { c ->
            TicketStatus.from(c.popFirstArg())
        }

        // Replacements
        commandReplacements.addReplacement("create", Config.aliasCreate)
        commandReplacements.addReplacement("update", Config.aliasUpdate)
        commandReplacements.addReplacement("close", Config.aliasClose)
        commandReplacements.addReplacement("show", Config.aliasShow)
        commandReplacements.addReplacement("pick", Config.aliasPick)
        commandReplacements.addReplacement("assign", Config.aliasAssign)
        commandReplacements.addReplacement("done", Config.aliasDone)
        commandReplacements.addReplacement("yield", Config.aliasYield)
        commandReplacements.addReplacement("note", Config.aliasNote)
        commandReplacements.addReplacement("reopen", Config.aliasReopen)
        commandReplacements.addReplacement("teleport", Config.aliasTeleport)
        commandReplacements.addReplacement("log", Config.aliasLog)
        commandReplacements.addReplacement("list", Config.aliasList)
        commandReplacements.addReplacement("status", Config.aliasStatus)
    }

    fun registerCompletions(ticketManager: TicketManager) {
        commandCompletions.registerAsyncCompletion("AllTicketHolders") {
            ticketManager.allKeys().map { uuid -> Bukkit.getOfflinePlayer(uuid) }.map { it.name }
        }

        commandCompletions.registerAsyncCompletion("UserTicketIds") { c ->
            try {
                ticketManager[c.getContextValue(OfflinePlayer::class.java).uniqueId].map { ticket -> ticket.id.toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("UserTicketIdsWithPlayer") { c ->
            try {
                ticketManager[c.getContextValue(OfflinePlayer::class.java, 1).uniqueId].map { ticket -> ticket.id.toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("UserTicketIdsWithTarget") { c ->
            try {
                ticketManager[c.getContextValue(OfflinePlayer::class.java, 2).uniqueId].map { ticket -> ticket.id.toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("IssuerTicketIds") { c ->
            ticketManager[c.issuer.uniqueId].map { ticket -> ticket.id.toString() }
        }

        commandCompletions.registerAsyncCompletion("UserNames") {
            try {
                SQLFunctions.retrieveTicketNames()
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("UserOfflineNames") {
            try {
                SQLFunctions.retrieveClosedTicketNames()
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("UserOfflineTicketIDs") { c ->
            try {
                SQLFunctions.retrieveClosedTicketIds(c.getContextValue(OfflinePlayer::class.java).uniqueId).map { it.toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerStaticCompletion("TicketStatus", TicketStatus.values().map { it.name.toLowerCase() })
    }

    fun registerInjections(vararg inputs: Any) {
        inputs.forEach {
            registerDependency(it::class.java, it)
        }
    }

    fun registerCommands() {
        registerCommand(TicketCommand())
        registerCommand(TicketsCommand())
        registerCommand(PureTicketsCommand())
    }

    private fun loadLocales() {
        File(plugin.dataFolder, "locales").listFiles()?.forEach { file ->
            val localeName = file.name.replace(".yml", "")
            val locale = Locale.forLanguageTag(localeName)

            val yamlConfiguration = YamlConfiguration()
            yamlConfiguration.load(file)

            val prefixables = TargetType.values().filter { it.hasPrefix }.map { it.name }
            val prefix = yamlConfiguration.getString("general.prefix")

            yamlConfiguration.getKeys(false).forEach { key ->
                if (prefixables.contains(key.toUpperCase())) {
                    yamlConfiguration.getConfigurationSection(key)?.getKeys(false)?.forEach { subKey ->
                        val path = key + "." + subKey
                        yamlConfiguration.set(path, prefix + yamlConfiguration.getString(path))
                    }
                }
            }

            addSupportedLanguage(locale)
            locales.loadLanguage(yamlConfiguration, locale)
        }
    }

    private fun saveLocales() {
        val localeFolder = File(plugin.dataFolder, "locales")
        val folder = plugin.javaClass.getResource("/locales/")

        localeFolder.mkdirs()

        val fs = FileSystems.newFileSystem(folder.toURI(), emptyMap<String, Any>())

        Files.walk(fs.getPath("/locales"))
            .filter { path -> path.toString().endsWith(".yml") }
            .forEach { path ->
                val target = File(localeFolder, path.fileName.toString())
                val stream = plugin.javaClass.getResourceAsStream(path.toString())

                if (!target.exists()) {
                    Files.copy(stream, target.absoluteFile.toPath())
                } else {
                    Utils.mergeYAML(stream, target)
                }
            }

        fs.close()
    }
}