package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.ConsoleUser;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VelocityConsoleUser extends ConsoleUser implements VelocityOnlineUser {

    private final CommandSource source;

    public VelocityConsoleUser(final @NonNull ConsoleCommandSource source) {
        super(source);
        this.source = source;
    }

    @Override
    public CommandSource source() {
        return this.source;
    }

}
