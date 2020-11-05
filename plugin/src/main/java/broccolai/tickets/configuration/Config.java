package broccolai.tickets.configuration;

import broccolai.corn.spigot.locale.LocaleUtils;
import cloud.commandframework.types.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;

@SuppressWarnings("unused")
public final class Config {

    private String LOCALE;
    private Integer REMINDER__DELAY;
    private Integer REMINDER__REPEAT;

    private Integer LIMIT__OPEN_TICKETS;

    private Boolean STORAGE__MYSQL;
    private String STORAGE__USER;
    private String STORAGE__PASSWORD;
    private String STORAGE__NAME;
    private String STORAGE__HOST;
    private Boolean STORAGE__SSL;

    private Boolean DISCORD__ENABLED;
    private String DISCORD__GUILD;
    private String DISCORD__TOKEN;
    private String DISCORD__NAME;

    private Pair<String, String[]> ALIAS__CREATE;
    private Pair<String, String[]> ALIAS__UPDATE;
    private Pair<String, String[]> ALIAS__CLOSE;
    private Pair<String, String[]> ALIAS__SHOW;
    private Pair<String, String[]> ALIAS__PICK;
    private Pair<String, String[]> ALIAS__ASSIGN;
    private Pair<String, String[]> ALIAS__DONE;
    private Pair<String, String[]> ALIAS__YIELD;
    private Pair<String, String[]> ALIAS__NOTE;
    private Pair<String, String[]> ALIAS__REOPEN;
    private Pair<String, String[]> ALIAS__TELEPORT;
    private Pair<String, String[]> ALIAS__LOG;
    private Pair<String, String[]> ALIAS__LIST;
    private Pair<String, String[]> ALIAS__STATUS;
    private Pair<String, String[]> ALIAS__HIGHSCORE;

    private String API__DOMAIN;

    /**
     * Initialise the Configuration wrapper with values.
     *
     * @param plugin the Plugin instance to use
     */
    public Config(final @NonNull Plugin plugin) {
        //noinspection rawtypes
        Class<Pair> pairClass = Pair.class;

        plugin.saveDefaultConfig();
        InputStream stream = plugin.getClass().getResourceAsStream("/config.yml");
        LocaleUtils.mergeYaml(stream, new File(plugin.getDataFolder(), "config.yml"));

        stream = plugin.getClass().getResourceAsStream("/config.yml");
        FileConfiguration config = plugin.getConfig();
        InputStreamReader streamReader = new InputStreamReader(stream);
        FileConfiguration sourceConfig = YamlConfiguration.loadConfiguration(streamReader);

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String fieldName = field.getName().toLowerCase().replace("__", ".");
            Object targetValue = config.get(fieldName, null);

            if (targetValue == null) {
                targetValue = sourceConfig.get(fieldName);
            }

            try {
                if (field.getType().isAssignableFrom(pairClass)) {
                    String unsplit = (String) targetValue;
                    String[] split = unsplit.split("\\|");
                    targetValue = Pair.of(split[0], Arrays.copyOfRange(split, 1, split.length));
                }

                field.set(this, targetValue);
            } catch (IllegalAccessException ignored) {
                Bukkit.getLogger().warning("PureTickets failed to set the " + fieldName + " configuration value");
            }
        }

        try {
            stream.close();
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().warning("PureTickets could not close stream");
        }
    }

    /**
     * @return Locale
     */
    public String getLocale() {
        return LOCALE;
    }

    /**
     * @return Reminder delay
     */
    public Integer getReminderDelay() {
        return REMINDER__DELAY;
    }

    /**
     * @return Reminder repeat
     */
    public Integer getReminderRepeat() {
        return REMINDER__REPEAT;
    }

    /**
     * @return Ticket limit
     */
    public Integer getTicketLimitOpen() {
        return LIMIT__OPEN_TICKETS;
    }

    /**
     * @return Storage SQL
     */
    public Boolean getStorageMySQL() {
        return STORAGE__MYSQL;
    }

    /**
     * @return Storage user
     */
    public String getStorageUser() {
        return STORAGE__USER;
    }

    /**
     * @return Storage password
     */
    public String getStoragePassword() {
        return STORAGE__PASSWORD;
    }

    /**
     * @return Storage name
     */
    public String getStorageName() {
        return STORAGE__NAME;
    }

    /**
     * @return Storage host
     */
    public String getStorageHost() {
        return STORAGE__HOST;
    }

    /**
     * @return Storage ssl
     */
    public Boolean getStorageSSL() {
        return STORAGE__SSL;
    }

    /**
     * @return Discord enabled
     */
    public Boolean getDiscordEnabled() {
        return DISCORD__ENABLED;
    }

    /**
     * @return Discord guild
     */
    public String getDiscordGuild() {
        return DISCORD__GUILD;
    }

    /**
     * @return Discord token
     */
    public String getDiscordToken() {
        return DISCORD__TOKEN;
    }

    /**
     * @return Discord name
     */
    public String getDiscordName() {
        return DISCORD__NAME;
    }

    /**
     * @return Alias create
     */
    public Pair<String, String[]> getAliasCreate() {
        return ALIAS__CREATE;
    }

    /**
     * @return Alias update
     */
    public Pair<String, String[]> getAliasUpdate() {
        return ALIAS__UPDATE;
    }

    /**
     * @return Alias close
     */
    public Pair<String, String[]> getAliasClose() {
        return ALIAS__CLOSE;
    }

    /**
     * @return Alias show
     */
    public Pair<String, String[]> getAliasShow() {
        return ALIAS__SHOW;
    }

    /**
     * @return Alias pick
     */
    public Pair<String, String[]> getAliasPick() {
        return ALIAS__PICK;
    }

    /**
     * @return Alias assign
     */
    public Pair<String, String[]> getAliasAssign() {
        return ALIAS__ASSIGN;
    }

    /**
     * @return Alias done
     */
    public Pair<String, String[]> getAliasDone() {
        return ALIAS__DONE;
    }

    /**
     * @return Alias yield
     */
    public Pair<String, String[]> getAliasYield() {
        return ALIAS__YIELD;
    }

    /**
     * @return Alias note
     */
    public Pair<String, String[]> getAliasNote() {
        return ALIAS__NOTE;
    }

    /**
     * @return Alias reopen
     */
    public Pair<String, String[]> getAliasReopen() {
        return ALIAS__REOPEN;
    }

    /**
     * @return Alias teleport
     */
    public Pair<String, String[]> getAliasTeleport() {
        return ALIAS__TELEPORT;
    }

    /**
     * @return Alias log
     */
    public Pair<String, String[]> getAliasLog() {
        return ALIAS__LOG;
    }

    /**
     * @return Alias list
     */
    public Pair<String, String[]> getAliasList() {
        return ALIAS__LIST;
    }

    /**
     * @return Alias status
     */
    public Pair<String, String[]> getAliasStatus() {
        return ALIAS__STATUS;
    }

    /**
     * @return Alias highscore
     */
    public Pair<String, String[]> getAliasHighscore() {
        return ALIAS__HIGHSCORE;
    }

    /**
     * @return Api domain
     */
    public String getApiDomain() {
        return API__DOMAIN;
    }

}
