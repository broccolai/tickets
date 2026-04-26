package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.model.profile.Profile;
import love.broccolai.tickets.minecraft.common.TicketPermissions;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.PlayerCommander;
import love.broccolai.tickets.minecraft.common.service.TicketOperations;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.parser.standard.StringParser;
import org.jspecify.annotations.NullMarked;

import static love.broccolai.tickets.api.model.TicketStatus.CLOSED;
import static love.broccolai.tickets.api.model.TicketStatus.OPEN;
import static love.broccolai.tickets.api.model.TicketStatus.PICKED;

@NullMarked
public final class StaffCommands extends AbstractCommand {

    private static final CloudKey<Ticket> TICKET_KEY = CloudKey.cloudKey("ticket", Ticket.class);
    private static final CloudKey<Profile> TARGET_KEY = CloudKey.cloudKey("profile", Profile.class);
    private static final CloudKey<String> MESSAGE_KEY = CloudKey.cloudKey("message", String.class);

    private final CommandArgumentFactory argumentFactory;
    private final TicketOperations actions;

    @Inject
    public StaffCommands(
        final CommandArgumentFactory argumentFactory,
        final TicketOperations actions
    ) {
        this.argumentFactory = argumentFactory;
        this.actions = actions;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<Commander> root = commandManager
            .commandBuilder("tickets", "tis");

        commandManager.command(
            root.literal("show")
                .permission(TicketPermissions.STAFF_SHOW)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.allOf(TicketStatus.class)))
                .handler(this::handleShow)
        );

        commandManager.command(
            root.literal("log")
                .permission(TicketPermissions.STAFF_SHOW)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.allOf(TicketStatus.class)))
                .handler(this::handleShow)
        );

        commandManager.command(
            root.literal("list")
                .permission(TicketPermissions.STAFF_LIST)
                .flag(this.statusFlag())
                .flag(this.playerFlag())
                .handler(this::handleList)
        );

        commandManager.command(
            root.literal("claim")
                .permission(TicketPermissions.STAFF_CLAIM)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(OPEN)))
                .handler(this::handleClaim)
        );

        commandManager.command(
            root.literal("assign")
                .permission(TicketPermissions.STAFF_ASSIGN)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(OPEN, PICKED)))
                .optional(TARGET_KEY, this.argumentFactory.profile())
                .handler(this::handleAssign)
        );

        commandManager.command(
            root.literal("unclaim")
                .permission(TicketPermissions.STAFF_UNCLAIM)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(PICKED)))
                .handler(this::handleUnclaim)
        );

        commandManager.command(
            root.literal("close", "done")
                .permission(TicketPermissions.STAFF_CLOSE)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(OPEN, PICKED)))
                .handler(this::handleClose)
        );

        commandManager.command(
            root.literal("reopen")
                .permission(TicketPermissions.STAFF_REOPEN)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.of(CLOSED)))
                .handler(this::handleReopen)
        );

        commandManager.command(
            root.literal("note")
                .permission(TicketPermissions.STAFF_NOTE)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.allOf(TicketStatus.class)))
                .required(MESSAGE_KEY, StringParser.greedyStringParser())
                .handler(this::handleNote)
        );

        commandManager.command(
            root.literal("teleport", "tp")
                .permission(TicketPermissions.STAFF_TELEPORT)
                .senderType(PlayerCommander.class)
                .required(TICKET_KEY, this.argumentFactory.targetedTicket(EnumSet.allOf(TicketStatus.class)))
                .handler(this::handleTeleport)
        );
    }

    private void handleShow(final CommandContext<Commander> context) {
        this.actions.show(context.sender(), context.get(TICKET_KEY));
    }

    private void handleList(final CommandContext<Commander> context) {
        this.actions.listStaff(
            context.sender(),
            this.statuses(context),
            context.flags().getValue("player")
        );
    }

    private void handleClaim(final CommandContext<Commander> context) {
        this.actions.claim(context.sender(), context.get(TICKET_KEY));
    }

    private void handleAssign(final CommandContext<Commander> context) {
        Commander commander = context.sender();
        UUID target = context.optional(TARGET_KEY)
            .map(Profile::uuid)
            .orElse(commander.uuid());

        this.actions.assign(commander, context.get(TICKET_KEY), target);
    }

    private void handleUnclaim(final CommandContext<Commander> context) {
        this.actions.unclaim(context.sender(), context.get(TICKET_KEY));
    }

    private void handleClose(final CommandContext<Commander> context) {
        this.actions.closeStaff(context.sender(), context.get(TICKET_KEY));
    }

    private void handleReopen(final CommandContext<Commander> context) {
        this.actions.reopen(context.sender(), context.get(TICKET_KEY));
    }

    private void handleNote(final CommandContext<Commander> context) {
        this.actions.note(context.sender(), context.get(TICKET_KEY), context.get(MESSAGE_KEY));
    }

    private void handleTeleport(final CommandContext<PlayerCommander> context) {
        this.actions.teleport(context.sender(), context.get(TICKET_KEY));
    }

    private CommandFlag<TicketStatus> statusFlag() {
        return CommandFlag.<Commander>builder("status")
            .withComponent(EnumParser.enumParser(TicketStatus.class))
            .build();
    }

    private CommandFlag<Profile> playerFlag() {
        return CommandFlag.<Commander>builder("player")
            .withComponent(this.argumentFactory.profile())
            .build();
    }

    private Set<TicketStatus> statuses(final CommandContext<Commander> context) {
        TicketStatus status = context.flags().getValue("status", null);

        if (status != null) {
            return EnumSet.of(status);
        }

        return EnumSet.of(OPEN, PICKED);
    }
}
