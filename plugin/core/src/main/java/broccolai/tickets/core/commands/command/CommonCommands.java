package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.user.UserService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CommonCommands implements BaseCommand {

    private final UserService userService;

    public CommonCommands(final @NonNull UserService userService) {
        this.userService = userService;
    }

    protected final void processShow(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {

        TextComponent.Builder component = Component.text();

        component.append(
                Component.text("Ticket ", TextColor.color(0xf5a5a5)),
                Component.text("#" + ticket.id(), ticket.status().color(), TextDecoration.BOLD),
                Component.newline()
        );

        Position position = ticket.position();

        component.append(
                Component.text("[" + ticket.status().name() + "]"),
                Component.text(" "),
                Component.text("[" + this.userService.name(ticket.player()) + "]"),
                Component.text(" "),
                Component.text("[" + position.x() + ", " + position.y() + ", " + position.z() + "]")
        );

        soul.sendMessage(component);
    }

    protected final void processLog(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
//        TextComponent.Builder component = Component.text()
//                .append(Message.TITLE__TICKET_LOG.use(ticket.templates()));
//
//        ticket.getMessages().forEach(message -> {
//            HoverEvent<Component> event;
//
//            if (message.getData() != null) {
//                event = HoverEvent.showText(
//                        Component.text(message.getData())
//                );
//            } else {
//                event = null;
//            }
//
//            component.append(Component.newline(), Message.FORMAT__LOG.use(message.templates()).hoverEvent(event));
//        });
//
//        soul.sendMessage(component);
    }

}
