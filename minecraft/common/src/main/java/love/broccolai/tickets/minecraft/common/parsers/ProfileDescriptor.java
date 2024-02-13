package love.broccolai.tickets.minecraft.common.parsers;

import com.google.inject.Inject;
import io.leangen.geantyref.TypeToken;
import java.util.regex.Pattern;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileDescriptor implements DescribedArgumentParser<Profile> {

    public static final CloudKey<Profile> LAST_FOUND_PROFILE = CloudKey.cloudKey("last_found_profile", Profile.class);
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

        Profile profile = this.profileService.get(input);
        commandContext.store(LAST_FOUND_PROFILE, profile);

        return ArgumentParseResult.success(profile);
    }

    @Override
    public Iterable<String> stringSuggestions(
        final CommandContext<Commander> commandContext,
        final CommandInput input
    ) {
        return this.profileService.onlineUsernames();
    }
}
