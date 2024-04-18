package love.broccolai.tickets.spring.inject;

import com.google.inject.AbstractModule;
import java.nio.file.Path;
import love.broccolai.tickets.api.service.ProfileService;
import love.broccolai.tickets.spring.service.SpringProfileService;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SpringPlatformModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Path.class).toInstance(Path.of("tickets-data"));
        this.bind(ProfileService.class).to(SpringProfileService.class);
    }

}
