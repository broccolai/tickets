package broccolai.tickets.core.inject.module;

import broccolai.tickets.core.factory.CloudArgumentFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class FactoryModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new FactoryModuleBuilder()
                .build(CloudArgumentFactory.class));
    }

}
