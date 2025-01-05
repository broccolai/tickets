package love.broccolai.tickets.minecraft.paper.parsers;

import io.leangen.geantyref.TypeToken;
import love.broccolai.tickets.minecraft.common.model.Commander;
import love.broccolai.tickets.minecraft.common.model.Location;
import love.broccolai.tickets.minecraft.common.parsers.LocationDescriptor;
import org.incendo.cloud.bukkit.parser.location.LocationParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperLocationDescriptor implements LocationDescriptor, BlockingSuggestionProvider.Strings<Commander> {
    private static final LocationParser<Commander> PARSER = new LocationParser<>();

    @Override
    public ArgumentParseResult<Location> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        return PARSER.parse(commandContext, commandInput)
            .mapSuccess(bukkitLocation -> new Location(
                bukkitLocation.getWorld().getName(),
                bukkitLocation.getX(),
                bukkitLocation.getY(),
                bukkitLocation.getZ(),
                bukkitLocation.getYaw(),
                bukkitLocation.getPitch()
            ));
    }

    @Override
    public TypeToken<Location> valueType() {
        return TypeToken.get(Location.class);
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        return PARSER.stringSuggestions(commandContext, input);
    }
}
