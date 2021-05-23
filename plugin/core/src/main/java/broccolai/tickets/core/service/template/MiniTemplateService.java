package broccolai.tickets.core.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class MiniTemplateService implements TemplateService {

    private final UserService userService;

    @Inject
    public MiniTemplateService(final @NonNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull List<@NonNull Template> player(
            final @NonNull String prefix,
            final @NonNull Soul soul
    ) {
        return Arrays.asList(
                Template.of(prefix, this.userComponent(soul.username(), soul.uuid())),
                Template.of(prefix + "_name", soul.username()),
                Template.of(prefix + "_uuid", soul.uuid().toString())
        );
    }

    @Override
    public @NonNull List<@NonNull Template> ticket(final @NonNull Ticket ticket) {
        String name = this.userService.snapshot(ticket.player()).username();
        return Arrays.asList(
                Template.of("ticket", Component.text('#', NamedTextColor.DARK_GRAY).append(Component.text(
                        ticket.id(), ticket.status().color(), TextDecoration.BOLD
                )).hoverEvent(HoverEvent.showText(Component.join(
                        Component.newline(),
                        Component.text("id: " + ticket.id()),
                        Component.text("player: " + name),
                        Component.text("status: " + ticket.status().name())
                ))).clickEvent(ClickEvent.runCommand("/tickets show " + ticket.id()))),
                Template.of("status", ticket.status().name()),
                Template.of("player", this.userComponent(name, ticket.player())),
//TODO
//                Template.of("position", TextComponent.ofChildren(
//                        Component.text("[", NamedTextColor.DARK_GRAY),
//                        Component.text(ticket.position().x(), NamedTextColor.YELLOW),
//                        Component.text(',', NamedTextColor.DARK_GRAY),
//                        Component.text(ticket.position().y(), NamedTextColor.YELLOW),
//                        Component.text(',', NamedTextColor.DARK_GRAY),
//                        Component.text(ticket.position().z(), NamedTextColor.YELLOW),
//                        Component.text("]", NamedTextColor.DARK_GRAY)
//                )),
                Template.of("message", ticket.message().message())
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
