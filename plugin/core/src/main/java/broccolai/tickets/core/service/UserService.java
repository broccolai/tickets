package broccolai.tickets.core.service;

import broccolai.tickets.api.model.user.OnlineSoul;

import broccolai.tickets.api.model.user.PlayerSoul;
import broccolai.tickets.api.model.user.Soul;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserService<S, P extends S> {

    @NonNull Soul wrap(@NonNull UUID uuid);

    @NonNull OnlineSoul wrap(@NonNull S sender);

    @NonNull PlayerSoul player(@NonNull P player);

    @NonNull UUID uuid(@NonNull S sender);

    @NonNull S sender(@NonNull UUID uuid);

}
