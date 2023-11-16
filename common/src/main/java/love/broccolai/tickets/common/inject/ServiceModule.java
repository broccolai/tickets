package love.broccolai.tickets.common.inject;

import com.google.inject.AbstractModule;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StatisticService;
import love.broccolai.tickets.api.service.StorageService;
import love.broccolai.tickets.common.service.CalculatingStatisticService;
import love.broccolai.tickets.common.service.DatabaseStorageService;
import love.broccolai.tickets.common.service.SimpleModificationService;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(StorageService.class).to(DatabaseStorageService.class);
        this.bind(ModificationService.class).to(SimpleModificationService.class);
        this.bind(StatisticService.class).to(CalculatingStatisticService.class);
    }
}
