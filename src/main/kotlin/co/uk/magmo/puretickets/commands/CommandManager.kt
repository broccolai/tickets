package co.uk.magmo.puretickets.commands

import co.aikar.commands.PaperCommandManager
import co.uk.magmo.puretickets.PureTickets.Companion.TICKETS
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.ticket.Message
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.TicketInformation
import co.uk.magmo.puretickets.ticket.TicketManager
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.Locale

class CommandManager : PaperCommandManager(TICKETS) {
    init {
        enableUnstableAPI("help")
        saveLocales()
        loadLocales()

        // Contexts
        commandContexts.registerContext(Message::class.java) { c ->
            Message(MessageReason.MESSAGE, c.joinArgs(), null, null)
        }

        commandContexts.registerContext(TicketInformation::class.java) { c ->
            TicketInformation(Bukkit.getOfflinePlayer(c.popFirstArg()).uniqueId, c.popFirstArg().toInt())
        }

        // Completions
        commandCompletions.registerAsyncCompletion("AllTicketHolders") {
            TicketManager.allKeys().map { uuid -> Bukkit.getOfflinePlayer(uuid) }.map { it.name }.toList()
        }

        commandCompletions.registerAsyncCompletion("UserTicketIds") { c ->
            IntRange(0, TicketManager[c.getContextValue(OfflinePlayer::class.java).uniqueId].size).map { it.toString() }.toList()
        }

        commandCompletions.registerAsyncCompletion("IssuerTicketIds") { c ->
            IntRange(0, TicketManager[c.issuer.uniqueId].size).map { (it + 1).toString() }.toList()
        }

        commandCompletions.registerAsyncCompletion("UserOfflineTicketIDs") { c ->
            SQLFunctions.retrieveClosedTicketIds(c.issuer.uniqueId).map { it.toString() }.toList()
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
            .filter { path -> path.endsWith(".yml") }
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