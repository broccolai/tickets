package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

public final class InteractionPlaceholderResolver implements IPlaceholderResolver<Audience, Interaction, Component> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

    private final UserService userService;

    @Inject
    public InteractionPlaceholderResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
            final String placeholderName,
            final Interaction interaction,
            final Audience receiver,
            final Type owner,
            final Method method,
            final @Nullable Object[] parameters
    ) {
        User user = this.userService.snapshot(interaction.sender());

        Component action = Component.text(interaction.action().toString());
        Component hover = Component.text("Time: " + FORMATTER.format(interaction.time()));

        if (interaction instanceof MessageInteraction messageInteraction) {
            hover = Component.join(
                    Component.newline(),
                    hover,
                    Component.text("Message: " + messageInteraction.message())
            );
        }

        return Map.of(
                placeholderName,
                Either.left(ConclusionValue.conclusionValue(action.hoverEvent(hover))),
                placeholderName + "_creator",
                Either.right(ContinuanceValue.continuanceValue(user, User.class))
        );
    }
}
