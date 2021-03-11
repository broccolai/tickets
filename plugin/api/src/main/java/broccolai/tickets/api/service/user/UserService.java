package broccolai.tickets.api.service.user;

import broccolai.tickets.api.model.user.OnlineSoul;
import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

public interface UserService<S, P extends S> {

    @NonNull Soul wrap(@NonNull UUID uuid);

    @NonNull Soul wrap(@NonNull String name);

    @NonNull OnlineSoul wrap(@NonNull S sender);

    @NonNull PlayerSoul player(@NonNull P player);

    @NonNull UUID uuid(@NonNull S sender);

    @NonNull Optional<S> sender(@NonNull UUID uuid);

    @NonNull Collection<PlayerSoul> players();

}
