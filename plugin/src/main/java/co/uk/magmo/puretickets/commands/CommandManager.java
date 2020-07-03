package co.uk.magmo.puretickets.commands;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import co.uk.magmo.puretickets.configuration.Config;
import co.uk.magmo.puretickets.locale.Messages;
import co.uk.magmo.puretickets.locale.TargetType;
import co.uk.magmo.puretickets.storage.TimeAmount;
import co.uk.magmo.puretickets.ticket.*;
import co.uk.magmo.puretickets.utilities.ListUtilities;
import co.uk.magmo.puretickets.utilities.NumberUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

public class CommandManager extends PaperCommandManager {
    public CommandManager(Plugin plugin, Config config, TicketManager ticketManager) {
        super(plugin);

        enableUnstableAPI("help");
        saveLocales();
        loadLocales();

        // Colours
        setFormat(MessageType.HELP, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY);
        setFormat(MessageType.INFO, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY);

        // Contexts
        getCommandContexts().registerOptionalContext(FutureTicket.class, c -> {
            FutureTicket future = new FutureTicket();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                String input = c.popFirstArg();

                if (input != null) {
                    Ticket ticket = ticketManager.get(Integer.parseInt(input));

                    if (ticket == null || (c.hasFlag("issuer")  && ticket.getPlayerUUID() != c.getPlayer().getUniqueId())) {
                        future.cancel(true);
                        c.getIssuer().sendInfo(Messages.EXCEPTIONS__TICKET_NOT_FOUND);
                        return;
                    }

                    future.complete(ticket);
                    return;
                }

                OfflinePlayer player;

                if (c.hasFlag("issuer")) {
                    player = (OfflinePlayer) c.getResolvedArg("player");
                } else {
                    player = (OfflinePlayer) c.getResolvedArg("offlinePlayer");
                }

                Ticket potentialTicket = ticketManager.getLatestTicket(player.getUniqueId());

                if (potentialTicket == null) {
                    future.cancel(true);
                    c.getIssuer().sendInfo(Messages.EXCEPTIONS__TICKET_NOT_FOUND);
                    return;
                }

                future.complete(potentialTicket);
            });

            return future;
        });

        getCommandContexts().registerContext(Message.class, c ->
                new Message(MessageReason.MESSAGE, LocalDateTime.now(), c.joinArgs())
        );

        getCommandContexts().registerContext(TicketStatus.class, c ->
                TicketStatus.from(c.popFirstArg())
        );

        getCommandContexts().registerContext(TimeAmount.class, c -> {
            try {
                return TimeAmount.valueOf(c.popFirstArg().toUpperCase());
            } catch (Exception e) {
                throw new InvalidCommandArgument();
            }
        });

        // Replacements
        getCommandReplacements().addReplacement("create", config.ALIAS__CREATE);
        getCommandReplacements().addReplacement("update", config.ALIAS__UPDATE);
        getCommandReplacements().addReplacement("close", config.ALIAS__CLOSE);
        getCommandReplacements().addReplacement("show", config.ALIAS__SHOW);
        getCommandReplacements().addReplacement("pick", config.ALIAS__PICK);
        getCommandReplacements().addReplacement("assign", config.ALIAS__ASSIGN);
        getCommandReplacements().addReplacement("done", config.ALIAS__DONE);
        getCommandReplacements().addReplacement("yield", config.ALIAS__YIELD);
        getCommandReplacements().addReplacement("note", config.ALIAS__NOTE);
        getCommandReplacements().addReplacement("reopen", config.ALIAS__REOPEN);
        getCommandReplacements().addReplacement("teleport", config.ALIAS__TELEPORT);
        getCommandReplacements().addReplacement("log", config.ALIAS__LOG);
        getCommandReplacements().addReplacement("list", config.ALIAS__LIST);
        getCommandReplacements().addReplacement("status", config.ALIAS__STATUS);
        getCommandReplacements().addReplacement("highscore", config.ALIAS__HIGHSCORE);
    }

    public void registerCompletions(TicketManager ticketManager) {
        getCommandCompletions().registerAsyncCompletion("TicketHolders", c ->
                ticketManager.allNames(TicketStatus.from(c.getConfig("status")))
        );

        getCommandCompletions().registerAsyncCompletion("TargetIds", c -> {
            try {
                OfflinePlayer target = c.getContextValue(OfflinePlayer.class, NumberUtilities.valueOfOrNull(c.getConfig("parameter")));
                TicketStatus status = TicketStatus.from(c.getConfig("status"));

                return ListUtilities.map(ticketManager.getIds(target.getUniqueId(), status), Object::toString);
            } catch (Exception e) {
                return null;
            }
        });

        getCommandCompletions().registerAsyncCompletion("IssuerIds", c -> {
            try {
                TicketStatus status = TicketStatus.from(c.getConfig("status"));

                return ListUtilities.map(ticketManager.getIds(c.getIssuer().getUniqueId(), status), Object::toString);
            } catch (Exception e) {
                return null;
            }
        });

        getCommandCompletions().registerStaticCompletion("TicketStatus", ListUtilities.map(Arrays.asList(TicketStatus.values()), value ->
                value.name().toLowerCase()
        ));

        getCommandCompletions().registerStaticCompletion("TimeAmounts", ListUtilities.map(Arrays.asList(TimeAmount.values()), value ->
                value.name().toLowerCase()
        ));
    }

    public void registerInjections(Object... inputs) {
        for (Object input : inputs) {
            registerDependency(input.getClass(), input);
        }
    }

    public void registerCommands() {
        registerCommand(new TicketCommand());
        registerCommand(new TicketsCommand());
//        registerCommand(PureTicketsCommand());
    }

    private void loadLocales() {
        File[] files = new File(plugin.getDataFolder(), "locales").listFiles();

        for (File file : files) {
            String localeName = file.getName().replace(".yml", "");
            Locale locale = Locale.forLanguageTag(localeName);

            YamlConfiguration yamlConfiguration = new YamlConfiguration();

            try {
                yamlConfiguration.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                Bukkit.getLogger().warning("Could not load locale file");
                return;
            }

            List<TargetType> filteredTargets = ListUtilities.filter(TargetType.values(), TargetType::getHasPrefix);
            List<String> prefixables = ListUtilities.map(filteredTargets, Enum::name);

            String prefix = yamlConfiguration.getString("general.prefix");

            for (String key : yamlConfiguration.getKeys(false)) {
                if (prefixables.contains(key.toUpperCase())) {
                    ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(key);

                    if (configurationSection == null) continue;

                    configurationSection.getKeys(false).forEach(subKey -> {
                        String path = key + "." + subKey;
                        yamlConfiguration.set(path, prefix + yamlConfiguration.getString(path));
                    });
                }
            }

            addSupportedLanguage(locale);
            locales.loadLanguage(yamlConfiguration, locale);
        }
    }

    private void saveLocales() {
        File localeFolder = new File(plugin.getDataFolder(), "locales");
        URL folder = plugin.getClass().getResource("/locales/");

        localeFolder.mkdirs();

        try {
            FileSystem fs = FileSystems.newFileSystem(folder.toURI(), new HashMap<>());

            Files.walk(fs.getPath("/locales"))
                    .filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> {
                        File target = new File(localeFolder, path.getFileName().toString());
                        InputStream stream = plugin.getClass().getResourceAsStream(path.toString());

                        if (!target.exists()) {
                            try {
                                Files.copy(stream, target.getAbsoluteFile().toPath());
                            } catch (IOException e) {
                                Bukkit.getLogger().warning("Could not save locale file");
                            }
                        } else {
//                            Utils.mergeYAML(stream, target);
                        }
                    });

            fs.close();
        } catch (IOException | URISyntaxException e) {
            Bukkit.getLogger().warning("Could not save locale file");
        }
    }
}