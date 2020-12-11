package broccolai.tickets.core.commands.command;

import broccolai.tickets.core.PureTickets;
import broccolai.tickets.core.locale.Message;
import broccolai.tickets.core.user.PlayerSoul;
import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.UserSettings;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
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
        Component component = Component.text("PureTickets: ")
                .append(Component.text(this.pureTickets.version()));
        c.getSender().sendMessage(component);
    }

    private void processSettings(final @NonNull CommandContext<Soul<C>> c) {
        PlayerSoul<?, ?> soul = (PlayerSoul<?, ?>) c.getSender();
        UserSettings.Options setting = c.get("setting");
        Boolean value = c.get("value");
        String status = value ? "enabled" : "disabled";

        soul.modifyPreferences(settings -> settings.set(c.get("setting"), c.get("value")));

        Component component = Message.FORMAT__SETTING_UPDATE.use(
          Template.of("setting", setting.name().toLowerCase()),
          Template.of("status", status)
        );
        soul.sendMessage(component);
    }

    private void processReload(final @NonNull CommandContext<Soul<C>> c) {
        this.pureTickets.stop();
        this.pureTickets.start();

        Component component = Component.text("PureTickets reloaded");
        c.getSender().sendMessage(component);
    }

}
