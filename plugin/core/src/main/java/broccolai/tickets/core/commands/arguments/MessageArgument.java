package broccolai.tickets.core.commands.arguments;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.core.model.interaction.BasicMessageInteraction;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Queue;
import java.util.StringJoiner;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class MessageArgument extends CommandArgument<OnlineSoul, MessageInteraction> {

    private MessageArgument(final boolean required, final @NonNull String name) {
        super(required, name, new MessageParser(), MessageInteraction.class);
    }

    public static @NonNull MessageArgument of(final @NonNull String name) {
        return new MessageArgument(true, name);
    }

    private static final class MessageParser implements ArgumentParser<OnlineSoul, MessageInteraction> {

        @Override
        public @NonNull ArgumentParseResult<MessageInteraction> parse(
                final @NonNull CommandContext<OnlineSoul> commandContext,
                final @NonNull Queue<String> inputQueue
        ) {
            String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NullPointerException("No input was provided"));
            }

            StringJoiner sj = new StringJoiner(" ");
            int size = inputQueue.size();

            for (int i = 0; i < size; ++i) {
                String string = inputQueue.peek();
                if (string == null) {
                    break;
                }

                sj.add(string);
                inputQueue.remove();
            }

            return ArgumentParseResult.success(new BasicMessageInteraction(
                    Action.MESSAGE,
                    LocalDateTime.now(ZoneId.systemDefault()),
                    commandContext.getSender().uuid(),
                    sj.toString()
            ));
        }

        @Override
        public boolean isContextFree() {
            return true;
        }

    }

}
