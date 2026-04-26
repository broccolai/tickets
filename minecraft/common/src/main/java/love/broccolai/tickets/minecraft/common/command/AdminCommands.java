package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import java.time.Duration;
import love.broccolai.tickets.minecraft.common.TicketPermissions;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.service.TicketOperations;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.standard.DurationParser;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AdminCommands extends AbstractCommand {

    private static final CloudKey<Duration> DURATION_KEY = CloudKey.cloudKey("duration", Duration.class);
    private static final Duration FOREVER_DURATION = Duration.ofSeconds(Long.MAX_VALUE);

    private final TicketOperations actions;

    @Inject
    public AdminCommands(final TicketOperations actions) {
        this.actions = actions;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<Commander> root = commandManager
            .commandBuilder("ticketsadmin");

        commandManager.command(
            root.literal("stats")
                .permission(TicketPermissions.ADMIN_STATS)
                .literal("lifespan")
                .optional("duration", DurationParser.durationParser())
                .handler(this::handleLifespan)
        );
    }

    private void handleLifespan(final CommandContext<Commander> context) {
        Commander commander = context.sender();
        Duration searchWindow = context.optional(DURATION_KEY)
            .orElse(FOREVER_DURATION);

        this.actions.averageLifespan(commander, searchWindow);
    }
}
