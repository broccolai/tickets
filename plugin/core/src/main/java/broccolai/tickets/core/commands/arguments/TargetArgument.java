package broccolai.tickets.core.commands.arguments;

import broccolai.corn.core.Lists;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class TargetArgument extends CommandArgument<OnlineSoul, Soul> {

    @Inject
    public TargetArgument(final @NonNull UserService userService, final @Assisted("name") @NonNull String name) {
        super(true, name, new TargetParser(userService), Soul.class);
    }

    private static final class TargetParser implements ArgumentParser<OnlineSoul, Soul> {

        private final UserService userService;

        private TargetParser(final @NonNull UserService userService) {
            this.userService = userService;
        }

        @Override
        public @NonNull ArgumentParseResult<@NonNull Soul> parse(
                final @NonNull CommandContext<@NonNull OnlineSoul> commandContext,
                final @NonNull Queue<@NonNull String> queue
        ) {
            String input = queue.peek();

            if (input == null || input.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TargetParser.class,
                        commandContext
                ));
            }

            queue.remove();
            return ArgumentParseResult.success(this.userService.wrap(input));
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(
                final @NonNull CommandContext<OnlineSoul> commandContext, final @NonNull String input
        ) {
            return Lists.map(this.userService.players(), PlayerSoul::username);
        }

    }

}
