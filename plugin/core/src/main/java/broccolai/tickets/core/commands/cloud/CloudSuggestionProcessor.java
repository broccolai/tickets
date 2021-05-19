package broccolai.tickets.core.commands.cloud;

import broccolai.tickets.api.model.user.OnlineSoul;
import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.List;

public final class CloudSuggestionProcessor implements CommandSuggestionProcessor<OnlineSoul> {

    @Override
    public @NonNull List<String> apply(
            @NonNull final CommandPreprocessingContext<OnlineSoul> context,
            @NonNull final List<String> strings
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
