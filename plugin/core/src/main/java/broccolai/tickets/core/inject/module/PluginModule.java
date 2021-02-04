package broccolai.tickets.core.inject.module;

import broccolai.tickets.core.inject.platform.TicketPlugin;
import broccolai.tickets.core.service.UserService;
import com.google.inject.AbstractModule;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PluginModule extends AbstractModule {

    private final TicketPlugin ticketPlugin;

    public PluginModule(final @NonNull TicketPlugin ticketPlugin) {
        this.ticketPlugin = ticketPlugin;
    }

    @Override
    protected void configure() {
        this.bind(UserService.class).to(this.ticketPlugin.userServiceClass());
    }

}
