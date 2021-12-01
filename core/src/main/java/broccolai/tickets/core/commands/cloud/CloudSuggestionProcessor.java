package broccolai.tickets.core.commands.cloud;

import broccolai.tickets.api.model.user.OnlineUser;
import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CloudSuggestionProcessor implements CommandSuggestionProcessor<OnlineUser> {

    @Override
    public @NonNull List<String> apply(
            final @NonNull CommandPreprocessingContext<OnlineUser> context,
            final @NonNull List<String> strings
    ) {
        String input;

        if (context.getInputQueue().isEmpty()) {
            input = "";
        } else {
            input = context.getInputQueue().peek();
        }

        input = input.toLowerCase();
        List<String> suggestions = new ArrayList<>();

        for (String suggestion : strings) {
            String lowercaseSuggestion = suggestion.toLowerCase();

            if (lowercaseSuggestion.startsWith(input)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

}
