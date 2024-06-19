package love.broccolai.tickets.minecraft.common.parsers;

import io.leangen.geantyref.TypeToken;
import java.util.List;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.standard.StringParser;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LabeledDescriptor implements DescribedArgumentParser<String> {

    private static final StringParser<Commander> PARSER = new StringParser<>(StringParser.StringMode.GREEDY);

    @Override
    public TypeToken<String> valueType() {
        return TypeToken.get(String.class);
    }

    @Override
    public ArgumentParseResult<String> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        return PARSER.parse(commandContext, commandInput);
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        return List.of("[<value>]");
    }
}
