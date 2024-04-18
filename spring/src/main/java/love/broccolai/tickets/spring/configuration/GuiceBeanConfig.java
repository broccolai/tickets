package love.broccolai.tickets.spring.configuration;

import com.google.inject.Injector;
import love.broccolai.tickets.common.TicketsPackage;
import love.broccolai.tickets.spring.inject.SpringPlatformModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuiceBeanConfig {

    private final Injector injector;

    {
        TicketsPackage ticketsPackage = new TicketsPackage();

        this.injector = ticketsPackage.startup(
            this.getClass().getClassLoader(),
            new SpringPlatformModule()
        );
    }

    @Bean
    public Injector injector() {
        return this.injector;
    }

}
