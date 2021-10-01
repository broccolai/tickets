package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.user.UserService;
import broccolai.tickets.core.utilities.Constants;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PureTicketsCommand implements BaseCommand {

    private final MessageService messageService;
    private final StorageService storageService;
    private final UserService userService;

    @Inject
    public PureTicketsCommand(
            final @NonNull MessageService messageService,
            final @NonNull StorageService storageService,
            final @NonNull UserService userService
    ) {
        this.messageService = messageService;
        this.storageService = storageService;
        this.userService = userService;
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
        ChronoUnit timeUnit = c.<ChronoUnit>getOptional("amount").orElse(ChronoUnit.YEARS);
        List<Component> components = new ArrayList<>();

        components.add(this.messageService.highscoreTitle());

        this.storageService.highscores(timeUnit).forEach((uuid, amount) -> {
            components.add(
                    this.messageService.highscoreEntry(
                            this.userService.snapshot(uuid),
                            amount
                    )
            );
        });

        sender.sendMessage(
                Component.join(
                        Component.newline(),
                        components
                )
        );
    }

}
