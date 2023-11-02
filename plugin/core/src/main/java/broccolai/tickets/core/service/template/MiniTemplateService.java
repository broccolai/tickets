package broccolai.tickets.core.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public final class MiniTemplateService implements TemplateService {

    private final UserService userService;

    @Inject
    public MiniTemplateService(final @NonNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NotNull List<@NonNull TagResolver> player(
            final @NonNull String prefix,
            final @NonNull Soul soul
    ) {
        return Arrays.asList(
                TagResolver.resolver(prefix, Tag.selfClosingInserting(this.userComponent(soul.username(), soul.uuid()))),
                TagResolver.resolver(prefix + "_name", Tag.preProcessParsed(soul.username())),
                TagResolver.resolver(prefix + "_uuid", Tag.preProcessParsed(soul.uuid().toString()))
        );
    }

    @Override
    public @NonNull List<@NonNull TagResolver> ticket(final @NonNull Ticket ticket) {
        String name = this.userService.name(ticket.player());

        TagResolver ticketResolver = TagResolver.resolver("ticket", Tag.selfClosingInserting(
                Component.text('#', NamedTextColor.DARK_GRAY)
                        .append(Component.text(
                                ticket.id(), ticket.status().color(), TextDecoration.BOLD
                        ))
                        .hoverEvent(HoverEvent.showText(Component.join(
                                Component.newline(),
                                Component.text("id: " + ticket.id()),
                                Component.text("player: " + name),
                                Component.text("status: " + ticket.status().name())
                        )))
                        .clickEvent(ClickEvent.runCommand("/tickets show " + ticket.id()))
        ));

        TagResolver positionResolver = TagResolver.resolver("position", Tag.selfClosingInserting(TextComponent.ofChildren(
                Component.text("[", NamedTextColor.DARK_GRAY),
                Component.text(ticket.position().x(), NamedTextColor.YELLOW),
                Component.text(',', NamedTextColor.DARK_GRAY),
                Component.text(ticket.position().y(), NamedTextColor.YELLOW),
                Component.text(',', NamedTextColor.DARK_GRAY),
                Component.text(ticket.position().z(), NamedTextColor.YELLOW),
                Component.text("]", NamedTextColor.DARK_GRAY)
        )));

        return Arrays.asList(
                ticketResolver,
                positionResolver,
                TagResolver.resolver("status", Tag.preProcessParsed(ticket.status().name())),
                TagResolver.resolver("player", Tag.selfClosingInserting(this.userComponent(name, ticket.player()))),
                TagResolver.resolver("message", Tag.preProcessParsed(ticket.message().message()))
        );
    }

    private Component userComponent(final @NonNull String name, final @NonNull UUID uuid) {
        return Component.text(name).hoverEvent(HoverEvent.showText(Component.join(
                Component.newline(),
                Component.text(name),
                Component.text(uuid.toString())
        )));
    }

}
