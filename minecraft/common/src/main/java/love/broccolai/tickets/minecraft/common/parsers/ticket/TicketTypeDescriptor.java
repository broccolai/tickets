package love.broccolai.tickets.minecraft.common.parsers.ticket;

import com.google.inject.Inject;
import io.leangen.geantyref.TypeToken;
import java.util.Locale;
import love.broccolai.tickets.api.model.format.TicketFormat;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.parsers.DescribedArgumentParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TicketTypeDescriptor implements DescribedArgumentParser<TicketFormat> {

    private final TicketTypeRegistry ticketTypeRegistry;

    @Inject
    public TicketTypeDescriptor(final TicketTypeRegistry ticketTypeRegistry) {
        this.ticketTypeRegistry = ticketTypeRegistry;
    }

    @Override
    public TypeToken<TicketFormat> valueType() {
        return TypeToken.get(TicketFormat.class);
    }

    @Override
    public ArgumentParseResult<TicketFormat> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        String input = commandInput.readString().toLowerCase(Locale.ROOT);
        TicketFormat type = this.ticketTypeRegistry.fromIdentifier(input);

        if (type == null) {
            return ArgumentParseResult.failure(new RuntimeException());
        }

        return ArgumentParseResult.success(type);
    }

    @Override
    public Iterable<String> stringSuggestions(final CommandContext<Commander> commandContext, final CommandInput input) {
        return this.ticketTypeRegistry.identifiers();
    }
}
