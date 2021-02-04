package broccolai.tickets.core.model.user;

import java.util.UUID;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

public interface UserAudience extends Identified, Identity, ForwardingAudience.Single {

    @Override
    @Pure
    @NonNull UUID uuid();

    @Pure
    @NonNull String username();

    @Override
    default @NonNull Identity identity() {
        return this;
    }

}
