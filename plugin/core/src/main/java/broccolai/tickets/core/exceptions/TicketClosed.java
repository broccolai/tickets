package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.io.Serial;

public final class TicketClosed extends PureException.Abstract {

    @Serial
    private static final long serialVersionUID = -1L;

    @Override
    public @NonNull Component message(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.exceptionTicketClosed();
    }

}
