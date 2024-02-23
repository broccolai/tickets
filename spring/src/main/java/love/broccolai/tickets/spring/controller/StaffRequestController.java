package love.broccolai.tickets.spring.controller;

import com.google.inject.Injector;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.EnumSet;

@RestController
public class StaffRequestController {

    private final StorageService storageService;

    public StaffRequestController(final Injector injector) {
        this.storageService = injector.getInstance(StorageService.class);
    }

    @GetMapping("/staff/list")
    public Collection<Ticket> list() {
        return this.storageService.findTickets(
            EnumSet.of(TicketStatus.OPEN),
            null,
            null
        );
    }
}
