package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.moonshine.placeholder.ConclusionValue;
import net.kyori.moonshine.placeholder.ContinuanceValue;
import net.kyori.moonshine.placeholder.IPlaceholderResolver;
import net.kyori.moonshine.util.Either;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TicketPlaceholderResolver implements IPlaceholderResolver<Audience, Ticket, Component> {

    private final UserService userService;

    @Inject
    public TicketPlaceholderResolver(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull Map<String, Either<ConclusionValue<? extends Component>, ContinuanceValue<?>>> resolve(
            final String placeholderName,
            final Ticket value,
            final Audience receiver,
            final Type owner,
            final Method method,
            final @Nullable Object[] parameters
    ) {
        Soul soul = this.userService.snapshot(value.player());
        int id = value.id();
        String message = value.interactions().findLatestMessage(msg -> msg.action() == Action.MESSAGE)
                .map(MessageInteraction::message)
                .orElseThrow();

        return Map.of(
                placeholderName,
                Either.left(ConclusionValue.conclusionValue(this.wholeTicket(value, soul))),
                placeholderName + "_id",
                Either.right(ContinuanceValue.continuanceValue(id, Number.class)),
                placeholderName + "_creator",
                Either.right(ContinuanceValue.continuanceValue(soul.username(), String.class)),
                placeholderName + "_message",
                Either.right(ContinuanceValue.continuanceValue(message, String.class))
        );
    }

    private @NonNull Component wholeTicket(final @NonNull Ticket ticket, final @NonNull Soul soul) {
        return Component.text('#', NamedTextColor.DARK_GRAY).append(Component.text(
                ticket.id(), ticket.status().color(), TextDecoration.BOLD
        )).hoverEvent(HoverEvent.showText(Component.join(
                Component.newline(),
                Component.text("id: " + ticket.id()),
                Component.text("player: " + soul.username()),
                Component.text("status: " + ticket.status().name())
        ))).clickEvent(ClickEvent.runCommand("/tickets show " + ticket.id()));
    }
}
