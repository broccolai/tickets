package broccolai.tickets.core.commands.arguments;

import broccolai.tickets.core.user.Soul;
import broccolai.tickets.core.user.User;
import broccolai.tickets.core.user.UserManager;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

public final class TargetArgument<C> extends CommandArgument<Soul<C>, User> {

    private TargetArgument(final @NonNull String name) {
        super(true, name, new TargetParser<>(), User.class);
    }

    /**
     * Create target argument
     *
     * @param name Arguments name
     * @param <C>  Command sender type
     * @return Target argument
     */
    public static <C> TargetArgument<C> of(final @NonNull String name) {
        return new TargetArgument<>(name);
    }

    private static final class TargetParser<C> implements ArgumentParser<Soul<C>, User> {

        @Override
        public @NonNull ArgumentParseResult<@NonNull User> parse(
                @NonNull final CommandContext<@NonNull Soul<C>> commandContext,
                @NonNull final Queue<@NonNull String> queue
        ) {
            String input = queue.peek();

            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        TargetParser.class,
                        commandContext
                ));
            }

            UserManager<C, ?, ?> userManager = commandContext.get("userManager");
            UUID uuid = userManager.getUniqueId(input);
            User user = userManager.getUser(uuid);

            queue.remove();
            return ArgumentParseResult.success(user);
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(
                @NonNull final CommandContext<Soul<C>> commandContext, @NonNull final String input
        ) {
            UserManager<C, ?, ?> userManager = commandContext.get("userManager");

            return userManager.getNames();
        }

    }

}
