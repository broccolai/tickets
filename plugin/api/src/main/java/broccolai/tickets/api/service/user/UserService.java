package broccolai.tickets.api.service.user;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Collection;
import java.util.UUID;

public interface UserService {

    @NonNull Soul wrap(@NonNull UUID uuid);

    @NonNull Soul wrap(@NonNull String name);

    @NonNull ConsoleSoul console();

    @NonNull PlayerSoul player(@NonNull UUID uuid);

    @NonNull Collection<PlayerSoul> players();

}
