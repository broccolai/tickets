package love.broccolai.tickets.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.sql.DataSource;
import love.broccolai.tickets.api.model.TicketType;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.api.registry.TicketTypeRegistry;
import love.broccolai.tickets.common.configuration.TicketsConfiguration;
import love.broccolai.tickets.common.inject.ConfigurationModule;
import love.broccolai.tickets.common.inject.ServiceModule;
import love.broccolai.tickets.common.packaged.PackagedActions;
import love.broccolai.tickets.common.packaged.PackagedMigrations;

public final class TicketsPackage {

    public Injector startup(
        final ClassLoader classLoader,
        final Module... additionalModules
    ) {
        Collection<Module> modules = this.integrateModules(
            additionalModules,
            new ConfigurationModule(),
            new ServiceModule()
        );

        Injector injector = Guice.createInjector(modules);

        PackagedActions.register(
            injector.getInstance(ActionRegistry.class)
        );

        PackagedMigrations.migrate(
            classLoader,
            injector.getInstance(DataSource.class)
        );

        TicketsConfiguration configuration = injector.getInstance(TicketsConfiguration.class);
        TicketTypeRegistry registry = injector.getInstance(TicketTypeRegistry.class);

        for (TicketType ticketType : configuration.types) {
            registry.register(ticketType.identifier(), ticketType);
        }

        return injector;
    }

    private Collection<Module> integrateModules(final Module[] additionalModules, final Module... baseModules) {
        Collection<Module> modules = new ArrayList<>();
        Collections.addAll(modules, baseModules);
        Collections.addAll(modules, additionalModules);

        return modules;
    }

}
