package broccolai.tickets.commands.arguments;

import broccolai.tickets.message.Message;
import broccolai.tickets.message.MessageReason;
import broccolai.tickets.user.Soul;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.StringJoiner;

public final class MessageArgument extends CommandArgument<Soul, Message> {

    private MessageArgument(final boolean required, final @NonNull String name) {
        super(required, name, new MessageParser(), Message.class);
    }

    /**
     * Create a new required command argument
     *
     * @param name Argument name
     * @return Created argument
     */
    public static @NonNull MessageArgument of(final @NonNull String name) {
        return new MessageArgument(true, name);
    }


    private static final class MessageParser implements ArgumentParser<Soul, Message> {

        @Override
        public @NonNull ArgumentParseResult<Message> parse(
                final @NonNull CommandContext<Soul> commandContext,
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

            return ArgumentParseResult.success(Message.create(MessageReason.MESSAGE, LocalDateTime.now(), sj.toString()));
        }

        @Override
        public boolean isContextFree() {
            return true;
        }

    }

}
