package broccolai.tickets.api.model.message;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public record TargetPair(
        @NonNull UUID uuid,
        @NonNull Component component
) {

}
