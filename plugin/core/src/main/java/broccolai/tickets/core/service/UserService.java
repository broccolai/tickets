package broccolai.tickets.core.service;

import broccolai.tickets.core.model.user.PlayerSoul;
import broccolai.tickets.core.model.user.OnlineSoul;

import broccolai.tickets.core.model.user.Soul;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserService<S, P extends S> {

    @NonNull Soul wrap(@NonNull UUID uuid);

    @NonNull OnlineSoul wrap(@NonNull S sender);

    @NonNull PlayerSoul player(@NonNull P player);

    @NonNull UUID uuid(@NonNull S sender);

    @NonNull S sender(@NonNull UUID uuid);

}
