package broccolai.tickets.core.inject.module;

import com.google.inject.AbstractModule;

public final class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new ConfigurationModule());
        this.install(new FactoryModule());
        this.install(new ServiceModule());
    }

}
