package broccolai.tickets.velocity.model;

import broccolai.tickets.api.model.user.ConsoleSoul;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VelocityConsoleSoul extends ConsoleSoul implements VelocityOnlineSoul {

    private final CommandSource source;

    public VelocityConsoleSoul(final @NonNull ConsoleCommandSource source) {
        super(source);
        this.source = source;
    }

    @Override
    public CommandSource source() {
        return this.source;
    }

}
