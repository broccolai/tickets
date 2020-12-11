package broccolai.tickets.core;

import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.integrations.DiscordManager;
import broccolai.tickets.core.interactions.NotificationManager;
import broccolai.tickets.core.storage.SQLPlatforms;
import broccolai.tickets.core.tasks.ReminderTask;
import broccolai.tickets.core.tasks.TaskManager;
import broccolai.tickets.core.ticket.TicketManager;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.UserManager;
import broccolai.tickets.core.utilities.TimeUtilities;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

@SuppressWarnings("unused")
public final class PureTickets<C, P extends C, S extends PlayerSoul<C, P>> {

    private final TicketsPlatform<C, P, S> platform;

    private TicketsEventBus eventBus;
    private TaskManager taskManager;
    private NotificationManager<?> notificationManager;
    private UserManager<C, P, S> userManager;
    private TicketManager ticketManager;

    /**
     * Start a PureTickets module
     *
     * @param platform Ticket platform instance
     */
    public PureTickets(final @NonNull TicketsPlatform<C, P, S> platform) {
        this.platform = platform;
    }

    /**
     * Start pure tickets
     */
    public void start() {
        eventBus = new TicketsEventBus();

        Config config = this.platform.getConfig(this);
        this.platform.copyLocales();
        this.platform.setupMessages(config);

        Jdbi jdbi = SQLPlatforms.construct(this.platform.getRootFolder(), config);

        taskManager = this.platform.getTaskManager();
        userManager = this.platform.getUserManager(eventBus, taskManager, jdbi);
        notificationManager = new NotificationManager<>(jdbi, eventBus, userManager);

        DiscordManager discordManager = new DiscordManager(this.platform.getLogger(), config);
        this.ticketManager = new TicketManager(eventBus, userManager, config, jdbi, taskManager);


        TicketsCommandManager<C> commandManager = this.platform.getCommandManager();
        commandManager.initialise(this, config, eventBus, userManager, ticketManager);

        taskManager.addRepeatingTask(new ReminderTask(userManager, ticketManager),
                TimeUtilities.minuteToLong(config.getReminderDelay()), TimeUtilities.minuteToLong(config.getReminderRepeat())
        );

        eventBus.registerListeners(userManager, notificationManager, ticketManager, ticketManager.getIdStorage());
    }

    /**
     * Disable pure tickets
     */
    public void stop() {
        if (this.eventBus != null) {
            this.eventBus.unregisterAll();
        }

        if (this.taskManager != null) {
            this.taskManager.clear();
        }

        if (this.notificationManager != null) {
            this.notificationManager.saveAll();
        }

        if (this.userManager != null) {
            this.userManager.saveAll();
        }

        if (this.ticketManager != null) {
            this.ticketManager.saveAll();
        }
    }

    /**
     * Get the version of PureTickets
     *
     * @return Version as string
     */
    public @NonNull String version() {
        return this.platform.getVersion();
    }

    /**
     * Get the ticket platform
     *
     * @return Ticket platform
     */
    public TicketsPlatform<C, P, S> getPlatform() {
        return platform;
    }

}
