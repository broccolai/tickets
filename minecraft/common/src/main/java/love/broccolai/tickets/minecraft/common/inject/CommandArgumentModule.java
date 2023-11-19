package love.broccolai.tickets.minecraft.common.inject;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import love.broccolai.tickets.common.inject.ServiceModule;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;

public final class CommandArgumentModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new ServiceModule());

        this.install(new FactoryModuleBuilder()
            .build(CommandArgumentFactory.class));
    }

}
