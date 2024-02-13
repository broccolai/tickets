package love.broccolai.tickets.minecraft.common.parsers;

import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DescribedArgumentParser<T> extends
    ParserDescriptor<Commander, T>,
    ArgumentParser<Commander, T>,
    BlockingSuggestionProvider.Strings<Commander> {

    @Override
    default ArgumentParser<Commander, T> parser() {
        return this;
    }

}
