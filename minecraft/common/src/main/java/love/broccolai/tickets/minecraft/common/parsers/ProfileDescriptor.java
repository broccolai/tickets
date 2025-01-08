package love.broccolai.tickets.minecraft.common.parsers;

import com.google.inject.Inject;
import io.leangen.geantyref.TypeToken;
import java.util.regex.Pattern;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileDescriptor implements DescribedArgumentParser<Profile>, BlockingSuggestionProvider.Strings<Commander> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

    private final ProfileService profileService;

    @Inject
    public ProfileDescriptor(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public TypeToken<Profile> valueType() {
        return TypeToken.get(Profile.class);
    }

    @Override
    public ArgumentParseResult<Profile> parse(
        final CommandContext<Commander> commandContext,
        final CommandInput commandInput
    ) {
        String input = commandInput.readString();

        if (!USERNAME_PATTERN.matcher(input).matches()) {
            return ArgumentParseResult.failure(new RuntimeException());
        }

        return this.profileService.get(input)
            .map(ArgumentParseResult::success)
            .orElse(ArgumentParseResult.failure(new RuntimeException("could not find profile")));
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        //todo: replace with a filter system
        return Trove.of(this.profileService.find())
            .map(Profile::username)
            .toList();
    }
}
