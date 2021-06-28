package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PureTicketsCommand implements BaseCommand {

    private final OldMessageService oldMessageService;
    private final StorageService storageService;

    @Inject
    public PureTicketsCommand(final @NonNull OldMessageService oldMessageService, final @NonNull StorageService storageService) {
        this.oldMessageService = oldMessageService;
        this.storageService = storageService;
    }

    @Override
    public void register(final @NonNull CommandManager<OnlineSoul> manager) {
        final Command.Builder<OnlineSoul> builder = manager.commandBuilder("puretickets");

        manager.command(builder.literal(
                "highscore",
                ArgumentDescription.of("View highscores of ticket completions"),
                "hs"
                )
                        .permission(Constants.STAFF_PERMISSION + ".highscore")
                        .argument(EnumArgument.optional(ChronoUnit.class, "amount"))
                        .handler(this::processHighscore)
                        .build()
        );
    }

    private void processHighscore(final @NonNull CommandContext<@NonNull OnlineSoul> c) {
        OnlineSoul sender = c.getSender();

        ChronoUnit amount = c.<ChronoUnit>getOptional("amount").orElse(ChronoUnit.YEARS);

        Map<UUID, Integer> highscores = this.storageService.highscores(amount);
        Component component = this.oldMessageService.commandsHighscore(highscores);

        sender.sendMessage(component);
    }

}
