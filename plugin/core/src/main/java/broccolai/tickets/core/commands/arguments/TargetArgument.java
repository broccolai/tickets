package broccolai.tickets.core.commands.arguments;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.Soul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;

import java.util.ArrayList;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class TargetArgument extends CommandArgument<OnlineSoul, Soul> {

    private TargetArgument(final @NonNull String name) {
        super(true, name, new TargetParser(), Soul.class);
    }

    /**
     * Create target argument
     *
     * @param name Arguments name
     * @param <C>  Command sender type
     * @return Target argument
     */
    public static <C> TargetArgument of(final @NonNull String name) {
        return new TargetArgument(name);
    }

    private static final class TargetParser implements ArgumentParser<OnlineSoul, Soul> {

        @Override
        public @NonNull ArgumentParseResult<@NonNull Soul> parse(
                @NonNull final CommandContext<@NonNull OnlineSoul> commandContext,
                @NonNull final Queue<@NonNull String> queue
        ) {
            String input = queue.peek();

            if (input == null || input.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TargetParser.class,
                        commandContext
                ));
            }
//
//            UserManager<?, ?, ?> userManager = commandContext.get("userManager");
//            UUID uuid = userManager.getUniqueId(input);
//            User user = userManager.getUser(uuid);

            queue.remove();
            return ArgumentParseResult.success(null);
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(
                @NonNull final CommandContext<OnlineSoul> commandContext, @NonNull final String input
        ) {
            return new ArrayList<>();
        }

    }

}
