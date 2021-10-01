package broccolai.tickets.api.service.user;

import broccolai.tickets.api.model.user.ConsoleSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.model.user.SoulSnapshot;
import java.util.Collection;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserService {

    @NonNull Soul wrap(@NonNull UUID uuid);

    @NonNull Soul wrap(@NonNull String name);

    @NonNull SoulSnapshot snapshot(@NonNull UUID name);

    @NonNull SoulSnapshot snapshot(@NonNull String name);

    @NonNull ConsoleSoul console();

    @NonNull PlayerSoul player(@NonNull UUID uuid);

    @NonNull Collection<PlayerSoul> players();

}
