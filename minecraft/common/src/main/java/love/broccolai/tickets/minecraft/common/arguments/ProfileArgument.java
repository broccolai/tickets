package love.broccolai.tickets.minecraft.common.arguments;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.suggestion.Suggestion;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.context.CommandInput;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import java.util.List;
import java.util.regex.Pattern;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.proflie.Profile;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.minecraft.common.model.Commander;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ProfileArgument extends CommandArgument<Commander, Profile> {

    @AssistedInject
    public ProfileArgument(
        final ProfileService profileService,
        final @Assisted("name") String name
    ) {
        super(name, new ProfileParser(profileService), Profile.class);
    }

    public static final class ProfileParser implements ArgumentParser<Commander, Profile> {

        private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

        private final ProfileService profileService;

        public ProfileParser(final ProfileService profileService) {
            this.profileService = profileService;
        }

        @Override
        public ArgumentParseResult<Profile> parse(
            final CommandContext<Commander> commandContext,
            final CommandInput inputQueue
        ) {
            String input = inputQueue.readString();

            if (!USERNAME_PATTERN.matcher(input).matches()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(ProfileArgument.class, commandContext));
            }

            Profile profile = this.profileService.get(input);

            return ArgumentParseResult.success(profile);
        }

        @Override
        public List<Suggestion> suggestions(final CommandContext<Commander> commandContext, final String input) {
            return Trove.of(this.profileService.onlineUsernames())
                .map(Suggestion::simple)
                .toList();
        }
    }

}
