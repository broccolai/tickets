package co.uk.magmo.puretickets.commands

import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.Locale

class CommandManager : PaperCommandManager(TICKETS) {
    init {
        enableUnstableAPI("help")
        saveLocales()
        loadLocales()

        // Colours
        setFormat(MessageType.HELP, ChatColor.AQUA, ChatColor.WHITE, ChatColor.DARK_GRAY)
        setFormat(MessageType.INFO, ChatColor.AQUA, ChatColor.WHITE, ChatColor.DARK_GRAY)

        // Contexts
        commandContexts.registerContext(Message::class.java) { c ->
            Message(MessageReason.MESSAGE, c.joinArgs(), null)
        }

        commandContexts.registerContext(TicketInformation::class.java) { c ->
            val name = c.popFirstArg()
            val index = c.popFirstArg() ?: "1"

            TicketInformation(Bukkit.getOfflinePlayer(name).uniqueId, index.toInt() - 1)
        }

        // Completions
        commandCompletions.registerAsyncCompletion("AllTicketHolders") {
            TicketManager.allKeys().map { uuid -> Bukkit.getOfflinePlayer(uuid) }.map { it.name }
        }

        commandCompletions.registerAsyncCompletion("UserTicketIds") { c ->
            try {
                TicketManager[c.getContextValue(OfflinePlayer::class.java).uniqueId].indices.map { it.inc().toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        commandCompletions.registerAsyncCompletion("IssuerTicketIds") { c ->
            TicketManager[c.issuer.uniqueId].indices.map { it.inc().toString() }
        }

        commandCompletions.registerAsyncCompletion("UserOfflineTicketIDs") { c ->
            try {
                SQLFunctions.retrieveClosedTicketIds(c.issuer.uniqueId).map { it.toString() }
            } catch (e: Exception) {
                return@registerAsyncCompletion null
            }
        }

        // Commands
        registerCommand(TicketCommand())
        registerCommand(TicketsCommand())
    }

    fun loadLocales() {
        File(TICKETS.dataFolder, "locales").listFiles()?.forEach { file ->
            val localeName = file.name.replace(".yml", "")
            val locale = Locale.forLanguageTag(localeName)

            addSupportedLanguage(locale)
            locales.loadYamlLanguageFile(file, locale)
        }
    }

    private fun saveLocales() {
        val localeFolder = File(TICKETS.dataFolder, "locales")
        val folder = TICKETS.javaClass.getResource("/locales/")

        localeFolder.mkdirs()

        val fs = FileSystems.newFileSystem(folder.toURI(), emptyMap<String, Any>())

        Files.walk(fs.getPath("/locales"))
            .filter { path -> path.toString().endsWith(".yml") }
            .forEach { path ->
                val target = File(localeFolder, path.fileName.toString())
                val stream = TICKETS.javaClass.getResourceAsStream(path.toString())

                if (!target.exists()) {
                    Files.copy(stream, target.absoluteFile.toPath())
                } else {
                    mergeLocales(stream, target)
                }
            }

        fs.close()
    }

    private fun mergeLocales(input: InputStream, destination: File) {
        val inputYaml = YamlConfiguration.loadConfiguration(InputStreamReader(input))
        val outputYaml = YamlConfiguration.loadConfiguration(destination)

        inputYaml.getKeys(true).forEach { path -> outputYaml[path] = outputYaml[path, inputYaml[path]] }
        outputYaml.save(destination)
    }
}