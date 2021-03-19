package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketNotFound extends PureException.Abstract {

    private static final long serialVersionUID = -1L;

    @Override
    public @NonNull Component message(@NonNull final MessageService messageService) {
        return messageService.exceptionTicketNotFound();
    }

}
