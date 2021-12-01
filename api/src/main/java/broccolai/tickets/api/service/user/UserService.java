package broccolai.tickets.api.service.user;

import broccolai.tickets.api.model.user.ConsoleUser;
import broccolai.tickets.api.model.user.PlayerUser;
import broccolai.tickets.api.model.user.User;
import broccolai.tickets.api.model.user.UserSnapshot;
import java.util.Collection;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserService {

    @NonNull User wrap(@NonNull UUID uuid);

    @NonNull User wrap(@NonNull String name);

    @NonNull UserSnapshot snapshot(@NonNull UUID name);

    @NonNull UserSnapshot snapshot(@NonNull String name);

    @NonNull ConsoleUser console();

    @NonNull PlayerUser player(@NonNull UUID uuid);

    @NonNull Collection<PlayerUser> players();

}
