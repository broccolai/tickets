package broccolai.tickets.core.configuration;

import broccolai.tickets.core.PureTickets;
import cloud.commandframework.types.tuples.Pair;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public abstract class Config {

    protected String LOCALE;
    protected Integer REMINDER__DELAY;
    protected Integer REMINDER__REPEAT;

    protected Integer LIMIT__OPEN_TICKETS;

    protected Boolean STORAGE__MYSQL;
    protected String STORAGE__USER;
    protected String STORAGE__PASSWORD;
    protected String STORAGE__NAME;
    protected String STORAGE__HOST;
    protected Boolean STORAGE__SSL;

    protected Boolean DISCORD__ENABLED;
    protected String DISCORD__GUILD;
    protected String DISCORD__TOKEN;
    protected String DISCORD__NAME;

    protected Pair<String, String[]> ALIAS__CREATE;
    protected Pair<String, String[]> ALIAS__UPDATE;
    protected Pair<String, String[]> ALIAS__CLOSE;
    protected Pair<String, String[]> ALIAS__SHOW;
    protected Pair<String, String[]> ALIAS__CLAIM;
    protected Pair<String, String[]> ALIAS__ASSIGN;
    protected Pair<String, String[]> ALIAS__DONE;
    protected Pair<String, String[]> ALIAS__UNCLAIM;
    protected Pair<String, String[]> ALIAS__NOTE;
    protected Pair<String, String[]> ALIAS__REOPEN;
    protected Pair<String, String[]> ALIAS__TELEPORT;
    protected Pair<String, String[]> ALIAS__LOG;
    protected Pair<String, String[]> ALIAS__LIST;
    protected Pair<String, String[]> ALIAS__STATUS;
    protected Pair<String, String[]> ALIAS__HIGHSCORE;

    protected String API__DOMAIN;

    /**
     * Initialise the Configuration wrapper with values.
     *
     * @param plugin the Plugin instance to use
     */
    public Config(final @NonNull PureTickets<?, ?, ?> plugin) {
        this.setup(plugin);
    }

    /**
     * Setup config
     *
     * @param pureTickets Pure Tickets instance
     */
    protected abstract void setup(@NonNull PureTickets<?, ?, ?> pureTickets);

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
    public Pair<String, String[]> getAliasClaim() {
        return ALIAS__CLAIM;
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
    public Pair<String, String[]> getAliasUnclaim() {
        return ALIAS__UNCLAIM;
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
