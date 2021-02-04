package broccolai.tickets.core.service;

import broccolai.tickets.core.model.user.PlayerUserAudience;
import broccolai.tickets.core.model.user.UserAudience;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface UserService<S, P extends S> {

    @NonNull UserAudience wrap(@NonNull S sender);

    @NonNull PlayerUserAudience playerAudience(@NonNull P player);

    @NonNull UUID uuid(@NonNull S sender);

    @NonNull P player(@NonNull S sender);

}
