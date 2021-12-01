package broccolai.tickets.api.model.user;

import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public record UserSnapshot(@NonNull UUID uuid, @NonNull String username) implements User {

}
