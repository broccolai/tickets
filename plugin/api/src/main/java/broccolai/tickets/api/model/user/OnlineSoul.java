package broccolai.tickets.api.model.user;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.UUID;

public interface OnlineSoul extends Soul, Identified, Identity, ForwardingAudience.Single {

    @Override
    @Pure
    @NonNull UUID uuid();

    @Override
    @Pure
    @NonNull String username();

    @Pure
    boolean permission(@NonNull String permission);

    @Override
    default @NonNull Identity identity() {
        return this;
    }

}
