package love.broccolai.tickets.spring.controller;

import com.google.inject.Injector;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import love.broccolai.corn.trove.Trove;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.model.TicketStatus;
import love.broccolai.tickets.api.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffRequestController {

    private final StorageService storageService;

    public StaffRequestController(final Injector injector) {
        this.storageService = injector.getInstance(StorageService.class);
    }

    @GetMapping("/staff/list")
    public Map<TicketStatus, Collection<Ticket>> list() {
        Collection<Ticket> tickets = this.storageService.findTickets(
            EnumSet.of(TicketStatus.OPEN, TicketStatus.CLOSED),
            null,
            null
        );

        return Trove.of(tickets).group(Ticket::status);
    }
}
