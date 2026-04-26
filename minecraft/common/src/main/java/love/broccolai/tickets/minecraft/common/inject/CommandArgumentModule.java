package love.broccolai.tickets.minecraft.common.inject;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import love.broccolai.tickets.common.inject.ServiceModule;
import love.broccolai.tickets.minecraft.common.factory.CommandArgumentFactory;
import love.broccolai.tickets.minecraft.common.service.ProfileSuggestionService;
import love.broccolai.tickets.minecraft.common.service.SimpleProfileSuggestionService;

public final class CommandArgumentModule extends AbstractModule {

    @Override
    protected void configure() {
        this.install(new ServiceModule());
        this.bind(ProfileSuggestionService.class).to(SimpleProfileSuggestionService.class);

        this.install(new FactoryModuleBuilder()
            .build(CommandArgumentFactory.class));
    }
}
