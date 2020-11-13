package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.locale.Messages;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserSettings;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PureTicketsCommand<C> {

    private final PureTickets<C, ?, ?> pureTickets;

    /**
     * Create a Pure Tickets Command
     *
     * @param pureTickets PureTickets instance
     * @param manager     Command Manager
     */
    public PureTicketsCommand(final @NonNull CommandManager<Soul<C>> manager, final @NonNull PureTickets<C, ?, ?> pureTickets) {
        this.pureTickets = pureTickets;

        final Command.Builder<Soul<C>> builder = manager.commandBuilder("puretickets", "pt");

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

    private void processInfo(final @NonNull CommandContext<Soul<C>> c) {
        c.getSender().message("PureTickets: " + this.pureTickets.version());
    }

    private void processSettings(final @NonNull CommandContext<Soul<C>> c) {
        PlayerSoul<?, ?> soul = (PlayerSoul<?, ?>) c.getSender();
        UserSettings.Options setting = c.get("setting");
        Boolean value = c.get("value");
        String status = value ? "enabled" : "disabled";

        soul.modifyPreferences(settings -> settings.set(c.get("setting"), c.get("value")));
        soul.message(Messages.OTHER__SETTING_UPDATE, "setting", setting.name().toLowerCase(), "status", status);
    }

    private void processReload(final @NonNull CommandContext<Soul<C>> c) {
        this.pureTickets.stop();
        this.pureTickets.start();
        c.getSender().message("PureTickets reloaded");
    }

}
