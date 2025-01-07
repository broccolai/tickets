package love.broccolai.tickets.minecraft.common.parsers;

import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DescribedArgumentParser<T> extends
    ParserDescriptor<Commander, T>,
    ArgumentParser<Commander, T> {

    @Override
    default ArgumentParser<Commander, T> parser() {
        return this;
    }

}
