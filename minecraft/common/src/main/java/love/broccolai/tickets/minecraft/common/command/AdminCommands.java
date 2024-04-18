package love.broccolai.tickets.minecraft.common.command;

import com.google.inject.Inject;
import java.time.Duration;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.utilities.DurationFormatter;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.standard.DurationParser;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public final class AdminCommands extends AbstractCommand {

    private final static CloudKey<Duration> DURATION_KEY = CloudKey.cloudKey("duration", Duration.class);
    private final static Duration FOREVER_DURATION = Duration.ofSeconds(Long.MAX_VALUE);

    private final StatisticService statisticService;

    @Inject
    public AdminCommands(final StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Override
    public void register(final CommandManager<Commander> commandManager) {
        Command.Builder<Commander> root = commandManager
            .commandBuilder("ticketsadmin");

        commandManager.command(
            root.literal("stats")
                .literal("lifespan")
                .optional("duration", DurationParser.durationParser())
                .handler(this::handleLifespan)
        );
    }

    public void handleLifespan(final CommandContext<Commander> context) {
        Commander commander = context.sender();
        Duration search = context.optional(DURATION_KEY)
            .orElse(FOREVER_DURATION);

        Duration result = this.statisticService.averageTicketsLifespan(search);
        String formattedResult = DurationFormatter.formatDuration(result);

        commander.sendMessage(
            text("average ticket lifespan: " + formattedResult)
        );
    }
}
