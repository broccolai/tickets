package broccolai.tickets.core.service.template;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.template.TemplateService;
import broccolai.tickets.api.service.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    public List<Template> player(@NonNull final UUID uuid) {
        String name = this.userService.name(uuid);
        return Arrays.asList(
                Template.of("player", this.userComponent(name, uuid)),
                Template.of("name", name),
                Template.of("uuid", uuid.toString())
        );
    }

    @Override
    public List<Template> ticket(@NonNull final Ticket ticket) {
        String name = this.userService.name(ticket.player());
        return Arrays.asList(
                Template.of("ticket", Component.text('#', NamedTextColor.DARK_GRAY).append(Component.text(
                        ticket.id(), ticket.status().color()
                )).hoverEvent(HoverEvent.showText(Component.join(
                        Component.newline(),
                        Component.text("id: " + ticket.id()),
                        Component.text("player: " + name),
                        Component.text("status: " + ticket.status().name())
                )))),
                Template.of("status", ticket.status().name()),
                Template.of("player", this.userComponent(name, ticket.player())),
                Template.of("position", TextComponent.ofChildren(
                        Component.text("[", NamedTextColor.DARK_GRAY),
                        Component.text(ticket.position().x(), NamedTextColor.YELLOW),
                        Component.text(',', NamedTextColor.DARK_GRAY),
                        Component.text(ticket.position().y(), NamedTextColor.YELLOW),
                        Component.text(',', NamedTextColor.DARK_GRAY),
                        Component.text(ticket.position().z(), NamedTextColor.YELLOW),
                        Component.text("]", NamedTextColor.DARK_GRAY)
                )),
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
