package broccolai.tickets.commands;

import broccolai.tickets.PureTickets;
import broccolai.tickets.exceptions.InvalidSettingType;
import broccolai.tickets.interactions.NotificationManager;
import broccolai.tickets.locale.Messages;
import broccolai.tickets.user.UserManager;
import broccolai.tickets.utilities.Constants;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

/**
 * Command used for information and per-user configuration.
 */
@CommandAlias("puretickets")
public class PureTicketsCommand extends BaseCommand {
    @Dependency
    private PureTickets plugin;
    @Dependency
    private UserManager userManager;
    @Dependency
    private NotificationManager notificationManager;

    /**
     * Retrieve information about the plugin.
     */
    @Default
    @Subcommand("info")
    public void onInfo(CommandIssuer issuer) {
        issuer.sendMessage(plugin.getName() + " " + plugin.getDescription().getVersion());
    }

    /**
     * Allows a player to change a particular per-user config.
     */
    @Subcommand("settings")
    @CommandPermission(Constants.USER_PERMISSION + ".settings")
    @CommandCompletion("announcements true|false")
    public void onSettings(Player player, String setting, @Optional Boolean value) {
        userManager.update(player.getUniqueId(), settings -> {
            //noinspection SwitchStatementWithTooFewBranches
            switch (setting.toLowerCase()) {
                case "announcements":
                    settings.setAnnouncements(value);
                    return settings;

                default:
                    throw new InvalidSettingType();
            }
        });

        String status = value ? "enabled" : "disabled";

        notificationManager.basic(player, Messages.OTHER__SETTING_UPDATE, "%setting%", setting, "%status%", status);
    }

    /**
     * Allows an operator to reload the plugins configurations.
     */
    @Subcommand("reload")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reload")
    public void onReload(CommandIssuer issuer) {
        plugin.onDisable();
        plugin.onEnable();
        issuer.sendMessage("PureTickets reloaded");
    }
}
