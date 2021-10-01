package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.MessageService;
import java.io.Serial;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TicketOpen extends PureException.Abstract {

    @Serial
    private static final long serialVersionUID = -1L;

    @Override
    public @NonNull Component message(final @NonNull MessageService messageService) {
        return messageService.exceptionTicketOpen();
    }

}
