package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.interaction.Action;
import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineUser;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.api.service.storage.StorageService;
import java.util.ArrayList;
import java.util.Collection;
import broccolai.tickets.api.service.user.UserService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CommonCommands implements BaseCommand {

    private final MessageService messageService;
    private final StorageService storageService;
    private final UserService userService;

    public CommonCommands(
            final @NonNull MessageService messageService,
            final @NonNull StorageService storageService,
            final @NonNull UserService userService
    ) {
        this.messageService = messageService;
        this.storageService = storageService;
        this.userService = userService;
    }

    protected final void processShow(final @NonNull OnlineUser soul, final @NonNull Ticket ticket) {
        Collection<Component> components = new ArrayList<>();

        components.add(this.messageService.showTitle());
        components.add(this.messageService.showFieldStatus(ticket.status()));
        components.add(this.messageService.showFieldCreator(this.userService.snapshot(ticket.uuid())));

        ticket.claimer().map(this.userService::snapshot)
                .ifPresent(claimer -> {
                    components.add(this.messageService.showFieldClaimer(claimer));
                });

        components.add(this.messageService.showFieldMessage(ticket
                .interactions()
                .findLatestMessage(message -> message.action() == Action.MESSAGE)
                .map(MessageInteraction::message)
                .orElseThrow()));
        components.add(this.messageService.showFieldStatus(ticket.status()));

        soul.sendMessage(
                Component.join(
                        Component.newline(),
                        components
                )
        );
    }

    protected final void processLog(final @NonNull OnlineUser soul, final @NonNull Ticket ticket) {
        Collection<Interaction> interactions = this.storageService.interactions(ticket);
        Collection<Component> components = new ArrayList<>();

        components.add(this.messageService.logTitle());

        for (final Interaction interaction : interactions) {
            components.add(this.messageService.logEntry(interaction));
        }

        soul.sendMessage(Component.join(
                Component.newline(),
                components
        ));
    }

}
