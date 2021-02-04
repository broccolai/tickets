package broccolai.tickets.bukkit;

import broccolai.tickets.bukkit.commands.BukkitTicketsCommandManager;
import broccolai.tickets.bukkit.configuration.BukkitConfig;
import broccolai.tickets.bukkit.service.BukkitUserService;
import broccolai.tickets.bukkit.tasks.BukkitTaskManager;
import broccolai.tickets.bukkit.user.BukkitPlayerSoul;
import broccolai.tickets.bukkit.user.BukkitUserManager;
import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.TicketsPlatform;
import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.service.UserService;
import broccolai.tickets.core.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;

@SuppressWarnings("unused")
public final class BukkitPlatform extends JavaPlugin implements TicketsPlatform<CommandSender, Player, BukkitPlayerSoul> {

    private final PureTickets<CommandSender, Player, BukkitPlayerSoul> pureTickets;

    /**
     * Create Bukkit platform
     */
    public BukkitPlatform() {
        this.pureTickets = new PureTickets<>(this);
    }

    @Override
    public void onEnable() {
        this.pureTickets.start();
    }

    @Override
    public void onDisable() {
        this.pureTickets.stop();
    }

    @Override
    public Config getConfig(@NonNull final PureTickets<?, ?, ?> pureTickets) {
        return new BukkitConfig(pureTickets);
    }

    @Override
    public void copyLocales() {
        File localeFolder = new File(this.getDataFolder(), "locales");
        URL sourceUrl = this.getClass().getResource("/locales/");

        //noinspection ResultOfMethodCallIgnored
        localeFolder.mkdirs();

        try (FileSystem fs = FileSystems.newFileSystem(sourceUrl.toURI(), new HashMap<>())) {
            Files.walk(fs.getPath("/locales"))
                    .filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> {
                        final String pathString = path.toString();
                        final Locale locale = Locale.forLanguageTag(pathString.substring(9, pathString.length() - 4));

                        final File target = new File(localeFolder, path.getFileName().toString());
                        final InputStream stream = this.getClass().getResourceAsStream(path.toString());

                        if (!target.exists()) {
                            try {
                                Files.copy(stream, target.getAbsoluteFile().toPath());
                            } catch (IOException e) {
                                Bukkit.getLogger().warning("Could not save locale file");
                            }
                        } else {
                            this.mergeYaml(stream, target);
                        }

                        try {
                            final FileConfiguration yaml = new YamlConfiguration();
                            yaml.load(target);
                        } catch (final InvalidConfigurationException | IOException e) {
                            Bukkit.getLogger().warning("Could not load locale file");
                        }
                    });
        } catch (final IOException | URISyntaxException e) {
            Bukkit.getLogger().warning("Could not save locale file");
        }
    }

    @Override
    public void mergeYaml(@NonNull final InputStream input, @NonNull final File destination) {
        try {
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            FileConfiguration inputYaml = YamlConfiguration.loadConfiguration(reader);
            FileConfiguration outputYaml = YamlConfiguration.loadConfiguration(destination);

            inputYaml.getKeys(true).forEach(path -> outputYaml.set(path, outputYaml.get(path, inputYaml.get(path))));
            outputYaml.save(destination);
            input.close();
            reader.close();
        } catch (final IOException e) {
            Bukkit.getLogger().warning("Could not merge yaml");
        }
    }

    @Override
    public void setupMessages(final @NonNull Config config) {
        String locale = config.getLocale();
        File localeFolder = new File(this.getDataFolder(), "locales");

        //noinspection ResultOfMethodCallIgnored
        localeFolder.mkdirs();
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(localeFolder, "locale_" + locale + ".yml"));

        Message.setup(configuration::getString);
    }

    @Override
    public BukkitUserManager getUserManager(
            @NonNull final TicketsEventBus eventManager,
            @NonNull final TaskManager taskManager,
            @NonNull final Jdbi jdbi
    ) {
        BukkitUserManager bukkitUserManager = new BukkitUserManager(this, eventManager, taskManager, jdbi);
        this.getServer().getPluginManager().registerEvents(bukkitUserManager, this);

        return bukkitUserManager;
    }

    @Override
    public @NonNull String getVersion() {
        return this.getDescription().getVersion();
    }

    @Override
    public @NotNull File getRootFolder() {
        return this.getDataFolder();
    }

    @Override
    public @NotNull TicketsCommandManager<CommandSender> getCommandManager() {
        return new BukkitTicketsCommandManager(this);
    }

    @Override
    public @NonNull TaskManager getTaskManager() {
        return new BukkitTaskManager(this);
    }

    @Override
    public @NonNull ClassLoader getCustomClassLoader() {
        return this.getClassLoader();
    }

    @Override
    public Class<? extends UserService<?, ?>> userServiceClass() {
        return BukkitUserService.class;
    }

}
