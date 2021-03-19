package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.MessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PureException {

    @NonNull Component message(@NonNull MessageService messageService);

    abstract class Abstract extends RuntimeException implements PureException {

        private static final long serialVersionUID = -1L;

    }

}
