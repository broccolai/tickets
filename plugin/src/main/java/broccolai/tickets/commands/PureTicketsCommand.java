package broccolai.tickets.commands;

import broccolai.tickets.locale.Messages;
import broccolai.tickets.user.PlayerSoul;
import broccolai.tickets.user.Soul;
import broccolai.tickets.user.UserSettings;
import broccolai.tickets.utilities.Constants;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Command used for information and per-user configuration.
 */
public class PureTicketsCommand {
    @NotNull
    private final Plugin plugin;

    /**
     * Create a Pure Tickets Command.
     *
     * @param plugin  Plugin instance
     * @param manager Command Manager
     */
    public PureTicketsCommand(@NotNull final Plugin plugin, @NotNull final CommandManager manager) {
        this.plugin = plugin;

        final Command.Builder<Soul> builder = manager.commandBuilder("puretickets", "pt");

        manager.command(builder.literal("info")
            .handler(this::processInfo));

        manager.command(builder.literal("settings")
            .permission(Constants.USER_PERMISSION + ".settings")
            .argument(EnumArgument.of(UserSettings.Options.class, "setting"))
            .argument(BooleanArgument.of("value"))
            .handler(this::processSettings));

        manager.command(builder.literal("reload")
            .permission(Constants.STAFF_PERMISSION + ".reload")
            .handler(this::processReload));
    }

    private void processInfo(@NotNull final CommandContext<Soul> c) {
        c.getSender().message(plugin.getName() + " " + plugin.getDescription().getVersion());
    }

    private void processSettings(@NotNull final CommandContext<Soul> c) {
        PlayerSoul soul = (PlayerSoul) c.getSender();
        UserSettings.Options setting = c.get("setting");
        Boolean value = c.get("value");
        String status = value ? "enabled" : "disabled";

        soul.modifyPreferences(settings -> settings.set(c.get("setting"), c.get("value")));
        soul.message(Messages.OTHER__SETTING_UPDATE, "setting", setting.name().toLowerCase(), "status", status);
    }

    private void processReload(@NotNull final CommandContext<Soul> c) {
        plugin.onDisable();
        plugin.onEnable();
        c.getSender().message("PureTickets reloaded");
    }
}
