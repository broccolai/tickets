package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.OldMessageService;
import java.io.Serial;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TooManyOpenTickets extends PureException.Abstract {

    @Serial
    private static final long serialVersionUID = -3338606171636696309L;

    @Override
    public @NonNull Component message(final @NonNull OldMessageService oldMessageService) {
        return oldMessageService.exceptionTooManyTicketsOpen();
    }

}
