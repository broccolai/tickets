package broccolai.tickets.core;

import broccolai.tickets.core.commands.TicketsCommandManager;
import broccolai.tickets.core.configuration.Config;
import broccolai.tickets.core.events.TicketsEventBus;
import broccolai.tickets.core.locale.LocaleManager;
import broccolai.tickets.core.tasks.TaskManager;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.UserManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.util.logging.Logger;

public interface TicketsPlatform<C, P extends C, S extends PlayerSoul<C, P>> {

    /**
     * Function to be called when platform is being enabled
     */
    void onEnable();

    /**
     * Function to be called when platform is being disabled
     */
    void onDisable();

    /**
     * Get the platforms config
     *
     * @param pureTickets Pure Tickets instance
     * @return Config
     */
    Config getConfig(@NonNull PureTickets<?, ?, ?> pureTickets);

    /**
     * @param config s
     */
    void setupMessages(@NonNull Config config);

    /**
     * Get the user manager
     *
     * @param eventManager  Event manager
     * @param taskManager   Task manager
     * @param localeManager Locale manager
     * @param jdbi          Jdbi instance
     * @return User manager
     */
    UserManager<C, P, S> getUserManager(
            @NonNull TicketsEventBus eventManager,
            @NonNull TaskManager taskManager,
            @NonNull LocaleManager localeManager,
            @NonNull Jdbi jdbi
    );

    /**
     * Get the PureTickets version
     *
     * @return Version as string
     */
    @NonNull String getVersion();

    /**
     * Get the roof folder of plugin storage / configuration
     *
     * @return File of root folder
     */
    @NonNull File getRootFolder();

    /**
     * Get the logger of the implementation
     *
     * @return Logger to use
     */
    @NonNull Logger getLogger();

    /**
     * @return Ticket manager
     */
    @NonNull TicketsCommandManager<C> getCommandManager();

    /**
     * Get the implementation of task manager
     *
     * @return Task manager
     */
    @NonNull TaskManager getTaskManager();

}
