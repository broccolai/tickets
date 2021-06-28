package broccolai.tickets.core.exceptions;

import broccolai.tickets.api.service.message.OldMessageService;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.io.Serial;

public interface PureException {

    @NonNull Component message(@NonNull OldMessageService oldMessageService);

    abstract class Abstract extends RuntimeException implements PureException {

        @Serial
        private static final long serialVersionUID = -1L;

    }

}
