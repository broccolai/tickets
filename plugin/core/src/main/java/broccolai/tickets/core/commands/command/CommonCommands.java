package broccolai.tickets.core.commands.command;

import broccolai.tickets.api.model.interaction.Interaction;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.service.message.OldMessageService;
import broccolai.tickets.api.service.storage.StorageService;
import java.util.Collection;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class CommonCommands implements BaseCommand {

    private final OldMessageService oldMessageService;
    private final StorageService storageService;

    public CommonCommands(
            final @NonNull OldMessageService oldMessageService,
            final @NonNull StorageService storageService
    ) {
        this.oldMessageService = oldMessageService;
        this.storageService = storageService;
    }

    protected final void processShow(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        soul.sendMessage(this.oldMessageService.showTicket(ticket));
    }

    protected final void processLog(final @NonNull OnlineSoul soul, final @NonNull Ticket ticket) {
        Collection<Interaction> interactions = this.storageService.interactions(ticket);
        soul.sendMessage(this.oldMessageService.commandsLog(interactions));
    }

}
