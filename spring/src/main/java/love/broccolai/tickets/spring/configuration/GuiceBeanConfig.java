package love.broccolai.tickets.spring.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.sql.DataSource;
import love.broccolai.tickets.api.registry.ActionRegistry;
import love.broccolai.tickets.common.inject.ConfigurationModule;
import love.broccolai.tickets.common.inject.ServiceModule;
import love.broccolai.tickets.common.packaged.PackagedActions;
import love.broccolai.tickets.common.packaged.PackagedMigrations;
import love.broccolai.tickets.spring.inject.SpringPlatformModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuiceBeanConfig {

    private final Injector injector;

    {
        this.injector = Guice.createInjector(
            new ConfigurationModule(),
            new ServiceModule(),
            new SpringPlatformModule()
        );

        PackagedActions.register(
            this.injector.getInstance(ActionRegistry.class)
        );

        PackagedMigrations.migrate(
            this.getClass().getClassLoader(),
            this.injector.getInstance(DataSource.class)
        );
    }

    @Bean
    public Injector injector() {
        return this.injector;
    }

}
