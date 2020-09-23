package broccolai.tickets.commands;

import broccolai.corn.core.Lists;
import broccolai.corn.core.Numbers;
import broccolai.corn.spigot.locale.LocaleUtils;
import broccolai.tickets.configuration.Config;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.locale.TargetType;
import broccolai.tickets.storage.TimeAmount;
import broccolai.tickets.storage.functions.TicketSQL;
import broccolai.tickets.ticket.FutureTicket;
import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.MessageReason;
import broccolai.tickets.ticket.Ticket;
import broccolai.tickets.ticket.TicketStatus;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * The Command Manager.
 */
public class CommandManager extends PaperCommandManager {
    /**
     * Initialise the Command Manager.
     *
     * @param plugin    the plugin to register commands against
     * @param config    the config instance to use
     */
    public CommandManager(@NotNull Plugin plugin, @NotNull Config config) {
        super(plugin);

        //noinspection deprecation
        enableUnstableAPI("help");
        saveLocales();
        loadLocales();

        getLocales().setDefaultLocale(Locale.forLanguageTag(config.LOCALE));

        // Colours
        setFormat(MessageType.HELP, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY);
        setFormat(MessageType.INFO, ChatColor.WHITE, ChatColor.AQUA, ChatColor.DARK_GRAY);

        registerContexts();
        registerReplacements(config);
        registerCompletions();
    }

    /**
     * Register command contexts.
     */
    public void registerContexts() {
        CommandContexts<BukkitCommandExecutionContext> commandContexts = getCommandContexts();
        commandContexts.registerOptionalContext(FutureTicket.class, c -> {
            FutureTicket future = new FutureTicket();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                String input = c.popFirstArg();

                if (input != null) {
                    Ticket ticket;

                    try {
                        int inputId = Integer.parseInt(input);
                        ticket = TicketSQL.select(inputId);
                    } catch (NumberFormatException e) {
                        ticket = null;
                    }

                    if (ticket == null || (c.hasFlag("issuer") && !ticket.getPlayerUUID().equals(c.getPlayer().getUniqueId()))) {
                        future.complete(null);
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

                List<TicketStatus> statuses = new ArrayList<>();

                if (c.hasAnnotation(AutoStatuses.class)) {
                    String value = c.getAnnotationValue(AutoStatuses.class);
                    String[] values = value.split(",");

                    for (String s : values) {
                        statuses.add(TicketStatus.valueOf(s));
                    }
                }

                Ticket potentialTicket = TicketSQL.selectLastTicket(player.getUniqueId(), statuses.toArray(new TicketStatus[0]));

                if (potentialTicket == null) {
                    future.complete(null);
                    c.getIssuer().sendInfo(Messages.EXCEPTIONS__TICKET_NOT_FOUND);
                    return;
                }

                future.complete(potentialTicket);
            });

            return future;
        });

        commandContexts.registerContext(Message.class, c ->
            new Message(MessageReason.MESSAGE, LocalDateTime.now(), c.joinArgs())
        );

        commandContexts.registerContext(TicketStatus.class, c ->
            TicketStatus.from(c.popFirstArg())
        );

        commandContexts.registerContext(TimeAmount.class, c -> {
            try {
                return TimeAmount.valueOf(c.popFirstArg().toUpperCase());
            } catch (Exception e) {
                throw new InvalidCommandArgument();
            }
        });
    }

    /**
     * Register command replacements.
     *
     * @param config the config instance to use
     */
    private void registerReplacements(@NotNull Config config) {
        CommandReplacements commandReplacements = getCommandReplacements();
        commandReplacements.addReplacement("create", config.ALIAS__CREATE);
        commandReplacements.addReplacement("update", config.ALIAS__UPDATE);
        commandReplacements.addReplacement("close", config.ALIAS__CLOSE);
        commandReplacements.addReplacement("show", config.ALIAS__SHOW);
        commandReplacements.addReplacement("pick", config.ALIAS__PICK);
        commandReplacements.addReplacement("assign", config.ALIAS__ASSIGN);
        commandReplacements.addReplacement("done", config.ALIAS__DONE);
        commandReplacements.addReplacement("yield", config.ALIAS__YIELD);
        commandReplacements.addReplacement("note", config.ALIAS__NOTE);
        commandReplacements.addReplacement("reopen", config.ALIAS__REOPEN);
        commandReplacements.addReplacement("teleport", config.ALIAS__TELEPORT);
        commandReplacements.addReplacement("log", config.ALIAS__LOG);
        commandReplacements.addReplacement("list", config.ALIAS__LIST);
        commandReplacements.addReplacement("status", config.ALIAS__STATUS);
        commandReplacements.addReplacement("highscore", config.ALIAS__HIGHSCORE);
    }

    /**
     * Register the commands completions.
     */
    private void registerCompletions() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = getCommandCompletions();
        commandCompletions.registerAsyncCompletion("TicketHolders", c ->
                TicketSQL.selectNames(TicketStatus.from(c.getConfig("status")))
        );

        commandCompletions.registerAsyncCompletion("TargetIds", c -> {
            try {
                OfflinePlayer target = c.getContextValue(OfflinePlayer.class, Numbers.valueOrNull(c.getConfig("parameter"), Integer::parseInt));
                TicketStatus status = TicketStatus.from(c.getConfig("status"));

                return Lists.map(TicketSQL.selectIds(target.getUniqueId(), status), Object::toString);
            } catch (Exception e) {
                return null;
            }
        });

        commandCompletions.registerAsyncCompletion("IssuerIds", c -> {
            try {
                TicketStatus status = TicketStatus.from(c.getConfig("status"));

                return Lists.map(TicketSQL.selectIds(c.getIssuer().getUniqueId(), status), Object::toString);
            } catch (Exception e) {
                return null;
            }
        });

        commandCompletions.registerStaticCompletion("TicketStatus", Lists.map(Arrays.asList(TicketStatus.values()), value ->
            value.name().toLowerCase()
        ));

        commandCompletions.registerStaticCompletion("TimeAmounts", Lists.map(Arrays.asList(TimeAmount.values()), value ->
            value.name().toLowerCase()
        ));
    }

    /**
     * Register dependencies to the commands.
     *
     * @param inputs the dependencies to register
     */
    public void registerInjections(@NotNull Object... inputs) {
        for (Object input : inputs) {
            registerDependency(input.getClass(), input);
        }
    }

    /**
     * Register commands.
     */
    public void registerCommands() {
        registerCommand(new TicketCommand());
        registerCommand(new TicketsCommand());
        registerCommand(new PureTicketsCommand());
    }

    private void loadLocales() {
        File[] files = new File(plugin.getDataFolder(), "locales").listFiles();

        for (File file : files) {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();

            try {
                yamlConfiguration.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                Bukkit.getLogger().warning("Could not load locale file");
                return;
            }

            List<TargetType> filteredTargets = Lists.filter(Arrays.asList(TargetType.values()), TargetType::getHasPrefix);
            List<String> prefixables = Lists.map(filteredTargets, Enum::name);

            prefixables.add("EXCEPTIONS");

            String prefix = yamlConfiguration.getString("general.prefix");

            for (String key : yamlConfiguration.getKeys(false)) {
                if (prefixables.contains(key.toUpperCase())) {
                    ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(key);

                    if (configurationSection == null) {
                        continue;
                    }

                    configurationSection.getKeys(false).forEach(subKey -> {
                        String path = key + "." + subKey;
                        yamlConfiguration.set(path, prefix + yamlConfiguration.getString(path));
                    });
                }
            }

            String localeName = file.getName().replace(".yml", "");
            Locale locale = Locale.forLanguageTag(localeName);

            addSupportedLanguage(locale);
            locales.loadLanguage(yamlConfiguration, locale);
        }
    }

    private void saveLocales() {
        File localeFolder = new File(plugin.getDataFolder(), "locales");
        URL folder = plugin.getClass().getResource("/locales/");

        //noinspection ResultOfMethodCallIgnored
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
                        LocaleUtils.mergeYaml(stream, target);
                    }
                });

            fs.close();
        } catch (IOException | URISyntaxException e) {
            Bukkit.getLogger().warning("Could not save locale file");
        }
    }
}