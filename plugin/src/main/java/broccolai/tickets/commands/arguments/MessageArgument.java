package broccolai.tickets.commands.arguments;

import broccolai.tickets.ticket.Message;
import broccolai.tickets.ticket.MessageReason;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;

public class MessageArgument<C> extends CommandArgument<C, Message> {
    public MessageArgument(boolean required, @NotNull String name) {
        super(required, name, new MessageParser<>(), Message.class);
    }

    public static final class MessageParser<C> implements ArgumentParser<C, Message> {
        @Override
        @NotNull
        public ArgumentParseResult<Message> parse(@NotNull CommandContext<C> commandContext, @NotNull Queue<String> inputQueue) {
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

            return ArgumentParseResult.success(new Message(MessageReason.MESSAGE, LocalDateTime.now(), sj.toString()));
        }

        @Override
        public boolean isContextFree() {
            return true;
        }
    }
}
